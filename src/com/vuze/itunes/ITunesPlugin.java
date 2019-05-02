/*
 * Created on Feb 6, 2009
 * Created by Paul Gardner
 * 
 * Copyright 2009 Vuze, Inc.  All rights reserved.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License only.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */


package com.vuze.itunes;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.*;

import com.biglybt.core.util.AESemaphore;
import com.biglybt.core.util.AEThread2;
import com.biglybt.core.util.Debug;
import com.biglybt.core.util.SystemTime;
import com.biglybt.pif.*;
import com.biglybt.pif.ipc.IPCException;
import com.biglybt.pif.logging.LoggerChannel;
import com.biglybt.pif.logging.LoggerChannelListener;
import com.biglybt.pif.ui.UIManager;
import com.biglybt.pif.ui.UIManagerEvent;
import com.biglybt.pif.ui.model.BasicPluginViewModel;
import com.biglybt.pif.utils.LocaleUtilities;

public class 
ITunesPlugin 
	implements Plugin
{
	private PluginInterface		plugin_interface;
	private LoggerChannel		logger;
	
	private ITunes itunes;
	
	private IPCException		init_error;
	
	private AESemaphore			task_sem 	= new AESemaphore( "iTunes:tasksem" );
	private List<iTunesTask<?>>	task_list	= new ArrayList<iTunesTask<?>>();
	
	private volatile boolean	destroyed;
	
	private int	last_installed		= -1;
	private int	last_running		= -1;
	
	private String	last_properties_error;
	private String	last_add_file_error;
	
	
	public void 
	initialize(
		PluginInterface _plugin_interface )
		
		throws PluginException 
	{
		plugin_interface	= _plugin_interface;
		
		LocaleUtilities loc_utils = plugin_interface.getUtilities().getLocaleUtilities();

		loc_utils.integrateLocalisedMessageBundle( "com.vuze.itunes.internat.Messages" );

		logger				= plugin_interface.getLogger().getTimeStampedChannel( "iTunes" ); 
		
		logger.setDiagnostic();

		UIManager	ui_manager = plugin_interface.getUIManager();
		
		final BasicPluginViewModel view_model = ui_manager.createBasicPluginViewModel( "iTunes" );
		
		view_model.getActivity().setVisible( false );
		view_model.getProgress().setVisible( false );
		
		logger.addListener(
				new LoggerChannelListener()
				{
					public void
					messageLogged(
						int		type,
						String	content )
					{
						view_model.getLogArea().appendText( content + "\n" );
					}
					
					public void
					messageLogged(
						String		str,
						Throwable	error )
					{
						if ( str.length() > 0 ){
							view_model.getLogArea().appendText( str + "\n" );
						}
						
						StringWriter sw = new StringWriter();
						
						PrintWriter	pw = new PrintWriter( sw );
						
						error.printStackTrace( pw );
						
						pw.flush();
						
						view_model.getLogArea().appendText( sw.toString() + "\n" );
					}
				});		
		
		try{
			String binaryPath = plugin_interface.getPluginDirectoryName();
			
			String newLibPath = binaryPath + File.pathSeparator	+ System.getProperty("java.library.path");

			System.setProperty( "java.library.path", newLibPath );
			
			Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );

			if ( fieldSysPath != null) {
				
				fieldSysPath.setAccessible(true);

				fieldSysPath.set( System.class.getClassLoader(), null );
			}
		}catch( Throwable e ){
			
			//init_error = new IPCException( "Failed to set library path", e );
			
			//log( "Initialisation failed", e );
			
			//return;
		}
			
		plugin_interface.addListener(
			new PluginListener()
			{
				public void
				initializationComplete()
				{
				}
				
				public void
				closedownInitiated()
				{
					try{
						new iTunesTask<Object>( "closedown", true )
						{
							public Object
							run()
							{
								destroyed	= true;
								
								if ( itunes != null ){
									
									itunes.destroy();
									
									itunes = null;
								}
								
								return( null );
							}
						}.queue(5*1000);
						
					}catch( Throwable e ){
						
						Debug.out( e );
						
					}finally{
						
						destroyed	= true;						
					}
				}
				
				public void
				closedownComplete()
				{
				}
			});
		
		new AEThread2( "iTunes:dispatcher", true )
		{
			public void
			run()
			{
				while( true ){
					
					task_sem.reserve();
					
					iTunesTask<?> task;
					
					synchronized( task_list ){
						
						if ( task_list.size() == 0 ){
						
								// can happen if task times out and gets removed
							
							continue;
						}
						
						task = task_list.remove(0);
					}
					
					task.execute();
				}
			}
		}.start();
	}
	
	public PluginInterface
	getPluginInterface()
	{
		return( plugin_interface );
	}
	
	public Map<String,Object>
	getProperties()
	
		throws IPCException
	{
		if ( init_error != null ){
			
			throw( init_error );
		}
		
		return(
			new iTunesTask<Map<String,Object>>( "getProperties" )
			{
				public Map<String,Object>
				run()
				{
					if ( itunes == null ){
						
						itunes = ITunesFactory.getITunesApplication( ITunesPlugin.this );
					}
					
					Map<String, Object>	properties = new HashMap<String, Object>();
					
					boolean installed = false;
					
					try{
						installed = itunes.isInstalled();
						
					}catch( ITunesCommunicationException e ){
					
						
					}
					
					properties.put( "installed", installed );
					
					int	i_installed = installed?1:0;
					
					if ( i_installed != last_installed ){
						
						last_installed = i_installed;
						
						log( "Installed=" + installed );
					}
					
					boolean running = false;
					
					try{
						running = installed?itunes.isRunning():false;
						
					}catch( ITunesCommunicationException e ){
						
					}
			
					properties.put( "running", running );
					
					int	i_running = running?1:0;
					
					if ( i_running != last_running ){
						
						last_running = i_running;
						
						log( "Running=" + running );
					}
					
					if ( running ){
						
						try{
							
							List<ITunesSource> i_sources = itunes.getSources();
						
							List<Map<String,Object>> p_sources = new ArrayList<Map<String,Object>>( i_sources.size());
							
							properties.put( "sources", p_sources );
							
							for ( ITunesSource i_source: i_sources ){
								
								Map<String,Object> p_source = new HashMap<String,Object>();
								
								p_sources.add( p_source );
								
								p_source.put( "id", new Long( i_source.getId()));
								
								p_source.put( "name", i_source.getName());	
							}
							
							last_properties_error = null;
							
						}catch( Throwable e ){
							
							String msg = Debug.getNestedExceptionMessage(e);
								
							if ( !msg.equals( last_properties_error )){
								
								last_properties_error = msg;
								
								log( "Failed to get properties", e );
							}
							
							properties.put( "error", e );
							
						}finally{
							
							if ( itunes != null ){
								
								itunes.destroy();
								
								itunes = null;
							}
						}
					}
					return( properties );
				}
			}.queue( 5000 ));
	}
	
	public Map<String,Object>
	addFileToLibrary(
		final File		file )
	
		throws IPCException
	{
		if ( init_error != null ){
			
			throw( init_error );
		}
		
		return(
			new iTunesTask<Map<String,Object>>( "addFile" )
			{
				public Map<String,Object>
				run()
				{
					Map<String, Object>	result = new HashMap<String, Object>();

					if ( itunes == null ){
						
						itunes = ITunesFactory.getITunesApplication( ITunesPlugin.this );
					}
					
					try{
						ITunesTrack track = itunes.getLibraryPlaylist().addFile( file.getAbsolutePath());	
	
						result.put( "id", new Long( track.getId()));
						
						// result.put( "index", new Long( track.getIndex()));
						
						result.put( "name", track.getName());	

						log( "Added " + file );
						
						last_add_file_error = null;
						
					}catch( Throwable e ){
						
						String msg = Debug.getNestedExceptionMessage(e);
						
						if ( !msg.equals( last_add_file_error )){
							
							last_add_file_error = msg;
							
							log( "Failed to add file", e );
						}
						
						result.put( "error", e );
						
						if ( itunes != null ){
							
							itunes.destroy();
							
							itunes = null;
						}
					}

					return( result );
				}
			}.queue( 60*1000 ));
	}
	
	public void
	restartRequired()
	{
		new AEThread2( "asyncify", true )
		{
			public void
			run()
			{
				plugin_interface.getUIManager().showMessageBox(
						"itunes.fixattempted.title",
						"itunes.fixattempted.details",
						UIManagerEvent.MT_OK );
			}
		}.start();
	}
	
	public void
	log(
		String		str )
	{
		logger.log( str );
	}
	
	public void
	log(
		String		str,
		Throwable	e )
	{
		logger.log( str, e );
	}
	
	private abstract class 
	iTunesTask<T>
	{
		private String	name;
		private boolean	force;
		
		private AESemaphore sem = new AESemaphore( "iTunes:wait" );

		private volatile T					result;
		private volatile IPCException		error;
		
		protected 
		iTunesTask(
			String		_name )
		{
			this( _name, false );
		}
		
		protected 
		iTunesTask(
			String		_name,
			boolean		_force )
		{
			name	= _name;
			force	= _force;
			
			synchronized( task_list ){

				task_list.add( this );
			}
			
			task_sem.release();
		}
		
		protected T
		queue(
			int		timeout )
		
			throws IPCException
		{
			if ( destroyed && !force ){
				
				throw( new IPCException( "Plugin closing" ));
				
			}
			if ( !sem.reserve( timeout )){
				
					// see if we can remove this task
				
				synchronized( task_list ){

					task_list.remove( this );
				}
				
				throw( new IPCException( "Operationed timeout" ));
			}
			
			if ( error != null ){
				
				throw( error );
			}
			
			return( result );
		}
		
		protected void
		execute()
		{
			try{
				if ( destroyed && !force ){
					
					throw( new IPCException( "Plugin closing" ));					
				}

				long	start = SystemTime.getMonotonousTime();
				
				result = run();
				
				long	elapsed = SystemTime.getMonotonousTime() - start;
				
				if ( elapsed > 500 ){
				
					log( "iTunes task duration for '" + name + "': " + elapsed );
				}
			}catch( Throwable e ){
				
				if ( e instanceof IPCException ){
					
					error = (IPCException)e;
					
				}else{
				
					error	= new IPCException( "Task failed", e );
				}
			}finally{
				
				sem.release();
			}
		}
		
		public abstract T
		run()
		
			throws IPCException;
	}
}
