package com.vuze.itunes.impl.osx.cocoa.applescript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple experiment with a builder class to build AS scripts in a statically typed manner
 *
 * @author olemarchand
 */
public final class ScriptBuilder {
    private ScriptBuilder() throws IllegalAccessException {
        throw new IllegalAccessException("no instanciation of this class!");
    }

    /**
     * creates a "tell application" type script
     *
     * @param applicationName the name of the application to communicate with
     * @return a {@link com.vuze.itunes.impl.osx.cocoa.applescript.ScriptBuilder.Tell} builder object
     */
    public static Tell tellApplication( String applicationName ) {
        return new Tell( "application", applicationName );
    }


    /**
     * creates a "tell application" type script wrapped in a timeout clause
     * if a time out occurs, an {@link com.vuze.itunes.impl.osx.cocoa.AppleScriptException} will be thrown during
     * execution of the script
     * 
     * @param applicationName the name of the application to communicate with
     * @return a {@link com.vuze.itunes.impl.osx.cocoa.applescript.ScriptBuilder.Tell} builder object
     */
    public static Tell tellApplication( String applicationName, int timeOutInSeconds ) {
        ASScriptBlockElement block = new ASScriptBlockElement( null, "with timeout of "+timeOutInSeconds+" seconds","end timeout");
        return block.append( new Tell( "application", applicationName, block ) );
    }

    public static interface ASScriptElement {
        public StringBuilder toScriptSource( StringBuilder builder, int indentation );

        public String toScriptSource();

        public ASScriptElement getRoot();
    }


    public static abstract class AbstractASScriptElement implements ASScriptElement {
        public static final String INDENTATION = "\t";
        public static final String NEW_LINE = "\n";

        protected final ASScriptElement parent;

        protected AbstractASScriptElement( ASScriptElement parent ) {
            this.parent = parent;
        }

        public String toScriptSource() {
            return getRoot().toScriptSource( new StringBuilder(), 0 ).toString();
        }

        public ASScriptElement getRoot() {
            if( parent == null ) {
                return this;
            } else {
                return parent.getRoot();
            }
        }

        protected void indent( StringBuilder sb, int indentation ) {
            for( int i = 0; i < indentation ; i++ ) {
                sb.append( INDENTATION );
            }
        }

        private static volatile int counter = 0;

        /**
         * static utility method to generate unique ids to be used in scripts
         */
        protected synchronized String uid(String seed){
            return "a"+seed+"_"+(++counter);
        }
    }

    public static abstract class AbstractASScriptElementContainer extends AbstractASScriptElement {
        protected final List<ASScriptElement> children;

        protected AbstractASScriptElementContainer( ASScriptElement parent ) {
            super( parent );
            this.children = new ArrayList<ASScriptElement>( );
        }

        protected void renderChildren( StringBuilder builder, int indentation ) {
            for( ASScriptElement child : children ) {
                child.toScriptSource( builder, indentation + 1 );
            }
        }

        protected <T extends ASScriptElement> T append(T e){
            this.children.add( e );
            return e;
        }
    }

    public static class ASScriptBlockElement extends AbstractASScriptElementContainer{
        private final String start;
        private final String end;

        public ASScriptBlockElement( ASScriptElement parent, String start, String end ) {
            super( parent );
            this.end = end;
            this.start = start;
        }

        public StringBuilder toScriptSource( StringBuilder builder, int indentation ) {
            indent( builder, indentation);
            builder.append( start ).append( NEW_LINE );
            renderChildren( builder, indentation );
            indent( builder, indentation);
            builder.append( end ).append( NEW_LINE );
            return builder;
        }
    }

    public static class Tell extends AbstractASScriptElementContainer {
        private final String object;
        private final String name;

        Tell( String object, String name ) {
            this( object, name, null );
        }

        Tell( String object, String name, ASScriptElement parent ) {
            super( parent );
            this.name = name;
            this.object = object;
        }

        public ASScriptElement returnResult( String variableName ) {
            return append( new RawStatement( variableName, this ) );
        }

        public StringBuilder toScriptSource( StringBuilder builder, int indentation ) {
            indent( builder, indentation );
            builder.append( "tell " ).append( object ).append( " \"" ).append( name ).append( "\"" + NEW_LINE );
            renderChildren( builder, indentation );
            indent( builder, indentation );
            builder.append( "end tell" + NEW_LINE );
            return builder;
        }

        public Loop forEach( String item ) {
            return append( new Loop( item, this ) );
        }

        public Tell tell(String item, String id){
            return append( new Tell( item, id, this ) );
        }
    }

    public static class RawStatement extends AbstractASScriptElement {
        private final String statement;

        RawStatement( String statement, ASScriptElement parent ) {
            super( parent );
            this.statement = statement;
        }

        public StringBuilder toScriptSource( StringBuilder builder, int indentation ) {
            indent( builder, indentation );
            builder.append( statement ).append( NEW_LINE );
            return builder;
        }
    }

    public static class StatementSequence extends AbstractASScriptElement {
        private final List<ASScriptElement> elts;

        StatementSequence(ASScriptElement parent, ASScriptElement...elts){
            super(parent );
            this.elts = new ArrayList<ASScriptElement>( );
            this.elts.addAll( Arrays.asList( elts ) );
        }

        public StringBuilder toScriptSource( StringBuilder builder, int indentation ) {
            for( ASScriptElement elt : elts ) {
                elt.toScriptSource( builder, indentation );
            }
            return builder;
        }

        protected ASScriptElement add(ASScriptElement e){
            this.elts.add( e );
            return e;
        }
    }

    public static class Loop extends StatementSequence{
        private final String itemToLoopOn;
        private final String resultVariable;
        private final String resultVariableRef;
        private final String loopVariable;

        Loop( String itemToLoopOn, ASScriptElement parent ) {
            super( parent );
            this.itemToLoopOn = itemToLoopOn;
            this.resultVariable = uid( "Result" );
            this.resultVariableRef = this.resultVariable + "Ref";
            this.loopVariable = uid( "item" );
        }

        public StringBuilder toScriptSource( StringBuilder builder, int indentation ) {
            indent( builder, indentation );
            builder.append( "set " ).append( resultVariable ).append( " to {}" + NEW_LINE );
            indent( builder, indentation );
            builder.append( "set " ).append( resultVariableRef ).append( " to a reference to " ).append( resultVariable ).append( NEW_LINE );
            indent( builder, indentation );
            builder.append( "repeat with " ).append( loopVariable ).append( " in " ).append( itemToLoopOn ).append( NEW_LINE );
            super.toScriptSource( builder, indentation + 1);
            indent( builder, indentation );
            builder.append( "end repeat" + NEW_LINE );
            indent( builder, indentation );
            builder.append( resultVariable ).append( NEW_LINE );
            return builder;
        }

        public Loop extract( String...projections ) {
            add( new RawStatement( "copy ({"+join(projections)+"} of "+loopVariable+") to the end of "+resultVariableRef, this ) );
            return this;
        }

        private String join( String...projections ) {
            StringBuilder b = new StringBuilder( );
            for( int i = 0; i < projections.length ; i++ ) {
                String projection = projections[i];
                if (i>0){
                    b.append( "," );
                }
                b.append( projection );
            }
            return b.toString();
        }

    }
}
