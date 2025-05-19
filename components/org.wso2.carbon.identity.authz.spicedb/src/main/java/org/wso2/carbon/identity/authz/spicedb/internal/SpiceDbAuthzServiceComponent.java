/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.authz.spicedb.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.authorization.framework.service.AccessEvaluationService;
import org.wso2.carbon.identity.authz.spicedb.handler.spicedb.SpicedbPermissionRequestService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * OSGi Component for the Spicedb Authorization Service.
 */
@Component(
        name = "identity.application.authz.spicedb.component",
        immediate = true
)
public class SpiceDbAuthzServiceComponent {

    private static final Log LOG = LogFactory.getLog(SpiceDbAuthzServiceComponent.class);
    private static RealmService realmservice;

    /**
     * Method to activate the component.
     *
     * @param context Context of the component
     */
    @Activate
    protected void activate (ComponentContext context) {

        try {
            SpicedbPermissionRequestService spicedbPermissionRequestService = new SpicedbPermissionRequestService();
            BundleContext bundleContext = context.getBundleContext();
            bundleContext.registerService(AccessEvaluationService.class, spicedbPermissionRequestService, null);
            LOG.debug("Application SpiceDB handler bundle is activated");
        } catch (Throwable throwable) {
            LOG.error("Error while starting spiceDB authorization component", throwable);
        }
    }

    /**
     * Method to deactivate the component.
     *
     * @param context Context of the component
     */
    @Deactivate
    protected void deactivate (ComponentContext context) {

        LOG.debug("Application SpiceDB handler bundle is deactivated.");
    }

    public static RealmService getRealmservice() {
        return realmservice;
    }

    /**
     * @param realmservice
     */
    public static void setRealmservice(RealmService realmservice) {

        SpiceDbAuthzServiceComponent.realmservice = realmservice;
    }

    /**
     * sets realm service.
     *
     * @param realmService <code>RealmService</code>
     */
    @Reference(
            name = "user.realmservice.default",
            service = org.wso2.carbon.user.core.service.RealmService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRealmService"
    )
    protected void setRealmService(RealmService realmService) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("DefaultUserRealm set in Entitlement bundle");
        }
        SpiceDbAuthzServiceComponent.realmservice = realmService;
    }

    /**
     * un-sets realm service.
     *
     * @param realmService <code>RealmService</code>
     */
    protected void unsetRealmService(RealmService realmService) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("DefaultUserRealm unset in Entitlement bundle");
        }
        SpiceDbAuthzServiceComponent.realmservice = null;
    }
}
