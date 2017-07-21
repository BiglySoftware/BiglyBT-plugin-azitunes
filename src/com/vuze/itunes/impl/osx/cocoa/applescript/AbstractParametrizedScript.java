package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.Script;

import java.util.Arrays;

/**
 * insert docs here :dawa:
 *
 * @author olemarchand
 */
public abstract class AbstractParametrizedScript implements Script {
    protected final String sourceCodeTemplate;
    protected final Object[] parameters;

    public AbstractParametrizedScript( String sourceCodeTemplate, Object... parameters ) {
        assert sourceCodeTemplate != null;
        this.sourceCodeTemplate = sourceCodeTemplate;
        this.parameters = parameters;
    }
    
    public String toString() {
        return this.getClass().getName()+"{" +
               "parameters=" + ( parameters == null ? null : Arrays.asList( parameters ) ) +
               ", sourceCodeTemplate='" + sourceCodeTemplate + '\'' +
               '}';
    }

    public abstract String getSourceCode();
}
