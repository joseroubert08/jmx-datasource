/*******************************************************************************
 * Copyright 2012 Adam Crume
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.adamcrume.jmxmon.el;

import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

public class SimpleContext extends ELContext {
    private FunctionMapper functionMapper;

    private CompositeELResolver resolver;

    private VariableMapper variableMapper;


    public SimpleContext(FunctionMapper functionMapper, CompositeELResolver resolver, VariableMapper variableMapper) {
        this.functionMapper = functionMapper;
        this.resolver = resolver;
        this.variableMapper = variableMapper;
    }


    @Override
    public VariableMapper getVariableMapper() {
        return variableMapper;
    }


    @Override
    public FunctionMapper getFunctionMapper() {
        return functionMapper;
    }


    @Override
    public ELResolver getELResolver() {
        return resolver;
    }
}
