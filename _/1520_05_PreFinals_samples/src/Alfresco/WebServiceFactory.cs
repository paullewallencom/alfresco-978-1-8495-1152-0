using System;
using System.Collections.Generic;
using System.Text;
using Alfresco.AuthenticationWebService;
using Alfresco.RepositoryWebService;
using Alfresco.ContentWebService;
using Alfresco.AccessControlWebService;
using Alfresco.ActionWebService;
using Alfresco.AdministrationWebService;
using Alfresco.AuthoringWebService;
using Alfresco.ClassificationWebService;
using Alfresco.DictionaryServiceWebService;
using Microsoft.Web.Services3;
using Microsoft.Web.Services3.Security;
using Microsoft.Web.Services3.Security.Tokens;
using Microsoft.Web.Services3.Security.Utility;

namespace Alfresco
{
    /// <summary>
    /// Web Service Factory
    /// 
    /// Convenience class that provides instances of the web service classes with the specified end-point 
    /// set and the security header added based on the information set in the AuthenticationUtils set (where
    /// appropriate)
    /// </summary>
    public class WebServiceFactory
    {
        /** Default endpoint address **/
        private const string DEFAULT_ENDPOINT_ADDRESS = "http://localhost:8080/alfresco";

        /** Current endpoint address **/
        private static string endPointAddress = DEFAULT_ENDPOINT_ADDRESS;

        /** Service addresses */
        private const string AUTHENTICATION_SERVICE_ADDRESS = "/api/AuthenticationService";
        private const string REPOSITORY_SERVICE_ADDRESS = "/api/RepositoryService";
        private const string CONTENT_SERVICE_ADDRESS = "/api/ContentService";
        private const string AUTHORING_SERVICE_ADDRESS = "/api/AuthoringService";
        private const string CLASSIFICATION_SERVICE_ADDRESS = "/api/ClassificationService";
        private const string ACTION_SERVICE_ADDRESS = "/api/ActionService";
        private const string ACCESS_CONTROL_SERVICE_ADDRESS = "/api/AccessControlService";
        private const string ADMINISTRATION_SERVICE_ADDRESS = "/api/AdministrationService";
        private const string DICTIONARY_SERVICE_ADDRESS = "/api/DictionaryService";

        /// <summary>
        /// Set the end point address used for all web service call's here-in
        /// </summary>
        /// <param name="endPointAddress"></param>
        /// <returns></returns>
        public static void setEndpointAddress(String endPointAddress)
        {
            WebServiceFactory.endPointAddress = endPointAddress;
        }

        /// <summary>
        /// Get the current end point address
        /// </summary>
        /// <returns></returns>
        public static String getEndpointAddress()
        {
            return WebServiceFactory.endPointAddress;
        }

        private static void addSecurityHeader(Microsoft.Web.Services3.WebServicesClientProtocol service)
        {
            UsernameToken userToken = new UsernameToken(AuthenticationUtils.UserName, AuthenticationUtils.Ticket, (PasswordOption)2);
            service.RequestSoapContext.Security.Timestamp.TtlInSeconds = (long)300;
            service.RequestSoapContext.Security.Tokens.Add(userToken);
        }

        /// <summary>
        /// Get the authentication service
        /// </summary>
        /// <returns></returns>
        public static AuthenticationService getAuthenticationService()
        {
            return getAuthenticationService(getEndpointAddress());
        }

        /// <summary>
        /// Get the authentication service
        /// </summary>
        /// <param name="endPointAddress"></param>
        /// <returns></returns>
        public static AuthenticationService getAuthenticationService(String endPointAddress)
        {
            AuthenticationService authenticationService = new AuthenticationService();
            authenticationService.Url = endPointAddress + AUTHENTICATION_SERVICE_ADDRESS;
            return authenticationService;
        }

        /// <summary>
        /// Get the respoitory service 
        /// </summary>
        /// <returns></returns>
        public static RepositoryService getRepositoryService()
        {
            return getRepositoryService(getEndpointAddress());
        }

        /// <summary>
        /// Get the repository service
        /// </summary>
        /// <param name="endPointAddress"></param>
        /// <returns></returns>
        public static RepositoryService getRepositoryService(String endPointAddress)
        {
            RepositoryService repositoryService = new RepositoryService();
            repositoryService.Url = endPointAddress + REPOSITORY_SERVICE_ADDRESS;
            addSecurityHeader(repositoryService);
            return repositoryService;
        }

        /// <summary>
        /// Get the content service
        /// </summary>
        /// <returns></returns>
        public static ContentService getContentService()
        {
            return getContentService(getEndpointAddress());
        }

        /// <summary>
        /// Get the content service
        /// </summary>
        /// <param name="endPointAddress"></param>
        /// <returns></returns>
        public static ContentService getContentService(String endPointAddress)
        {
            ContentService contentService = new ContentService();
            contentService.Url = endPointAddress + CONTENT_SERVICE_ADDRESS;
            addSecurityHeader(contentService);
            return contentService;
        }

        public static AuthoringService getAuthoringService()
        {
            return getAuthoringService(getEndpointAddress());
        }

        public static AuthoringService getAuthoringService(String endPointAddress)
        {
            AuthoringService authoringService = new AuthoringService();
            authoringService.Url = endPointAddress + AUTHORING_SERVICE_ADDRESS;
            addSecurityHeader(authoringService);
            return authoringService;
        }

        public static ClassificationService getClassificationService()
        {
            return getClassificationService(getEndpointAddress());
        }

        public static ClassificationService getClassificationService(String endPointAddress)
        {
            ClassificationService classificationService = new ClassificationService();
            classificationService.Url = endPointAddress + CLASSIFICATION_SERVICE_ADDRESS;
            addSecurityHeader(classificationService);
            return classificationService;
        }

        public static ActionService getActionService()
        {
            return getActionService(getEndpointAddress());
        }

        public static ActionService getActionService(String endPointAddress)
        {
            ActionService actionService = new ActionService();
            actionService.Url = endPointAddress + ACTION_SERVICE_ADDRESS;
            addSecurityHeader(actionService);
            return actionService;
        }

        public static AccessControlService getAccessControlService()
        {
            return getAccessControlService(getEndpointAddress());
        }

        public static AccessControlService getAccessControlService(String endPointAddress)
        {
            AccessControlService accessControlService = new AccessControlService();
            accessControlService.Url = endPointAddress + ACCESS_CONTROL_SERVICE_ADDRESS;
            addSecurityHeader(accessControlService);
            return accessControlService;
        }

        public static AdministrationService getAdministrationService()
        {
            return getAdministrationService(getEndpointAddress());
        }

        public static AdministrationService getAdministrationService(String endPointAddress)
        {
            AdministrationService administrationService = new AdministrationService();
            administrationService.Url = endPointAddress + ADMINISTRATION_SERVICE_ADDRESS;
            addSecurityHeader(administrationService);
            return administrationService;
        }

        public static DictionaryService getDictionaryService()
        {
            return getDictionaryService(getEndpointAddress());
        }

        public static DictionaryService getDictionaryService(String endPointAddress)
        {
            DictionaryService dictionaryService = new DictionaryService();
            dictionaryService.Url = endPointAddress + DICTIONARY_SERVICE_ADDRESS;
            addSecurityHeader(dictionaryService);
            return dictionaryService;
        }
    }
}
