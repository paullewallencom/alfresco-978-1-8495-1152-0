using System;
using System.Collections.Generic;
using System.Text;
using Alfresco.AuthenticationWebService;

namespace Alfresco
{
    public class Constants
    {
    	public const String QUERY_LANG_LUCENE	= "lucene";
    	
        /** Namespace constants */
        public const String NAMESPACE_SYSTEM_MODEL   = "http://www.alfresco.org/model/system/1.0";
        public const String NAMESPACE_CONTENT_MODEL  = "http://www.alfresco.org/model/content/1.0";
        
        /** Useful model constants */
        public static readonly String ASSOC_CHILDREN =         createQNameString(NAMESPACE_SYSTEM_MODEL, "children");
        public static readonly String TYPE_CMOBJECT = createQNameString(NAMESPACE_CONTENT_MODEL, "cmobject");
        public static readonly String PROP_NAME = createQNameString(NAMESPACE_CONTENT_MODEL, "name");
        public static readonly String TYPE_CONTENT = createQNameString(NAMESPACE_CONTENT_MODEL, "content");
        public static readonly String PROP_CONTENT = createQNameString(NAMESPACE_CONTENT_MODEL, "content");
        public static readonly String ASSOC_CONTAINS = createQNameString(NAMESPACE_CONTENT_MODEL, "contains");
        public static readonly String ASPECT_VERSIONABLE = createQNameString(NAMESPACE_CONTENT_MODEL, "versionable");
        public static readonly String ASPECT_TITLED = createQNameString(NAMESPACE_CONTENT_MODEL, "titled");
        public static readonly String PROP_CREATED = createQNameString(NAMESPACE_CONTENT_MODEL, "created");
        public static readonly String PROP_DESCRIPTION = createQNameString(NAMESPACE_CONTENT_MODEL, "description");
        public static readonly String PROP_TITLE = createQNameString(NAMESPACE_CONTENT_MODEL, "title");
        public static readonly String TYPE_FOLDER = createQNameString(NAMESPACE_CONTENT_MODEL, "folder");
        public static readonly String ASPECT_CLASSIFIABLE = createQNameString(NAMESPACE_CONTENT_MODEL, "classifiable"); 
        
        /** Person property constants */
        public static readonly String PROP_USERNAME = createQNameString(NAMESPACE_CONTENT_MODEL, "userName");
        public static readonly String PROP_USER_HOMEFOLDER = createQNameString(NAMESPACE_CONTENT_MODEL, "homeFolder");
        public static readonly String PROP_USER_FIRSTNAME = createQNameString(NAMESPACE_CONTENT_MODEL, "firstName");
        public static readonly String PROP_USER_MIDDLENAME = createQNameString(NAMESPACE_CONTENT_MODEL, "middleName");
        public static readonly String PROP_USER_LASTNAME = createQNameString(NAMESPACE_CONTENT_MODEL, "lastName");
        public static readonly String PROP_USER_EMAIL = createQNameString(NAMESPACE_CONTENT_MODEL, "email");
        public static readonly String PROP_USER_ORGID = createQNameString(NAMESPACE_CONTENT_MODEL, "organizationId");
        
        /** Permission prefixes for role's and group's */
        public const String ROLE_PREFIX      = "ROLE_";    
        public const String GROUP_PREFIX     = "GROUP_";
        
        /** Standard authorities */
        public const String ALL_AUTHORITIES          = "GROUP_EVERYONE";
        public const String OWNER_AUTHORITY          = "ROLE_OWNER";
        public const String LOCK_OWNER_AUTHORITY     = "ROLE_LOCK_OWNER";
        public const String ADMINISTRATOR_AUTHORITY  = "ROLE_ADMINISTRATOR";

        /** Common permissions */
        public const String ALL_PERMISSIONS          = "All";
        public const String FULL_CONTROL             = "FullControl";
        public const String READ                     = "Read";
        public const String WRITE                    = "Write";
        public const String DELETE                   = "Delete";
        public const String ADD_CHILDREN             = "AddChildren";
        public const String READ_PROPERTIES          = "ReadProperties";
        public const String READ_CHILDREN            = "ReadChildren";
        public const String WRITE_PROPERTIES         = "WriteProperties";
        public const String DELETE_NODE              = "DeleteNode";
        public const String DELETE_CHILDREN          = "DeleteChildren";
        public const String CREATE_CHILDREN          = "CreateChildren";
        public const String LINK_CHILDREN            = "LinkChildren";
        public const String DELETE_ASSOCIATIONS      = "DeleteAssociations";
        public const String READ_ASSOCIATIONS        = "ReadAssociations";
        public const String CREATE_ASSOCIATIONS      = "CreateAssociations";
        public const String READ_PERMISSIONS         = "ReadPermissions";
        public const String CHANGE_PERMISSIONS       = "ChangePermissions";
        public const String EXECUTE                  = "Execute";
        public const String READ_CONTENT             = "ReadContent";
        public const String WRITE_CONTENT            = "WriteContent";
        public const String EXECUTE_CONTENT          = "ExecuteContent";
        public const String TAKE_OWNERSHIP           = "TakeOwnership";
        public const String SET_OWNER                = "SetOwner";
        public const String COORDINATOR              = "Coordinator";
        public const String CONTRIBUTOR              = "Contributor";
        public const String EDITOR                   = "Editor";
        public const String GUEST                    = "Guest";
        public const String LOCK                     = "Lock";   
        public const String UNLOCK                   = "Unlock";
        public const String CHECK_OUT                = "CheckOut";
        public const String CHECK_IN                 = "CheckIn";
        public const String CANCEL_CHECK_OUT         = "CancelCheckOut";

        /**
         * Helper function to create a QName string from a namespace URI and name
         * 
         * @param namespace     the namespace URI
         * @param name          the name
         * @return              QName string
         */
        public static String createQNameString(String namespaceValue, String name)
        {
            return "{" + namespaceValue + "}" + name;
        }
    }
}
