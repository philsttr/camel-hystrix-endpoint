/*
 * Copyright 2015, Jollydays GmbH
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
 *
 * HystrixEndpoint.java
 *
 * Authors:
 *     Roman Mohr (r.mohr@jollydays.com)
**/

package com.jollydays.camel;

import org.apache.camel.*;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

@UriEndpoint(scheme = "hystrix", syntax = "hystrix:childUri", title = "hystrix component", producerOnly = true)
public class HystrixEndpoint extends DefaultEndpoint implements DelegateEndpoint {

    private final String childUri;

    @UriParam(name = "hystrixGroup")
    @Metadata(required = "true")
    private String group;

    @UriParam(name = "hystrixCommand")
    @Metadata(required = "false")
    private String command;

    @UriParam(name = "hystrixCommandTimeout")
    @Metadata(required = "false")
    private Integer timeout;

    public HystrixEndpoint(final String endpointUri, final String remainingUri, final Component component) {
        super(endpointUri, component);
        childUri = remainingUri;
    }

    @Override
    public Producer createProducer() throws Exception {
        return new HystrixProducer(this, getCamelContext().getEndpoint(childUri).createProducer(), group, command, timeout);
    }

    @Override
    public Consumer createConsumer(final Processor processor) throws Exception {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public Endpoint getEndpoint() {
        return getCamelContext().getEndpoint(childUri);
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    @Override
    public boolean isLenientProperties() {
        return true;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
