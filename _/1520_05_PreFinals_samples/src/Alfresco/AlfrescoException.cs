using System;
using System.Collections.Generic;
using System.Text;

namespace Alfresco
{
    class AlfrescoException : Exception
    {
        public AlfrescoException(String message)
            : base(message)
        {
        }
    }
}
