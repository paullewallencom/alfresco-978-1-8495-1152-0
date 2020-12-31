using System;
using System.Collections.Generic;
using System.Text;
using Alfresco.AuthenticationWebService;

namespace Alfresco
{
    /// <summary>
    /// Authentication Utils
    /// </summary>
    public class AuthenticationUtils
    {
        /// <summary>
        /// The current ticket stored per thread
        /// </summary>
        [ThreadStatic]
        private static string currentTicket;

        /// <summary>
        /// The current user name stored per thread
        /// </summary>
        [ThreadStatic]
        private static string currentUserName;

        /// <summary>
        /// The current ticket
        /// </summary>
        public static string Ticket
        {
            get
            {
                return AuthenticationUtils.currentTicket;
            }
        }

        /// <summary>
        /// The current user name
        /// </summary>
        public static string UserName
        {
            get
            {
                return AuthenticationUtils.currentUserName;
            }
        }

        /// <summary>
        /// Indicates whether we currently have a ticket for the current session.
        /// 
        /// NOTE: we could do with a isSessionValid method on the authentication service so that we can check whether the 
        /// stored ticket is still valid or not.
        /// </summary>
        public static bool IsSessionValid
        {
            get
            {
                return (AuthenticationUtils.currentTicket != null);
            }
        }

        /// <summary>
        /// Starts the session
        /// </summary>
        /// <param name="userName"></param>
        /// <param name="password"></param>
        public static void startSession(string userName, string password)
        {
            // Try and authenticate the user and then store the results in the thread static members
            AuthenticationResult results = WebServiceFactory.getAuthenticationService().startSession(userName, password);
            AuthenticationUtils.currentTicket = results.ticket;
            AuthenticationUtils.currentUserName = results.username;
        }

        /// <summary>
        /// Ends the session
        /// </summary>
        public static void endSession()
        {
            if (AuthenticationUtils.currentTicket != null)
            {
                WebServiceFactory.getAuthenticationService().endSession(AuthenticationUtils.currentTicket);
                AuthenticationUtils.currentTicket = null;
                AuthenticationUtils.currentUserName = null;
            }
        }
    }
}
