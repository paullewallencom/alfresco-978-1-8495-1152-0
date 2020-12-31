using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;

namespace Alfresco
{
    /// <summary>
    /// Mimetype map singleton class, enabling lookup for mimetypes and extensions.
    /// Used mimetypes.xml configuration file.
    /// </summary>
    public class MimetypeMap 
    {
        /// <summary>
        /// Location of the config file used to load the mime type details
        /// </summary>
        private const String CONFIG_FILE_LOCATION = ".\\Config\\mimetypes.xml";

        // Common mimetypes
        public const String MIMETYPE_TEXT_PLAIN = "text/plain";
        public const String MIMETYPE_TEXT_CSS = "text/css";    
        public const String MIMETYPE_XML = "text/xml";
        public const String MIMETYPE_HTML = "text/html";
        public const String MIMETYPE_XHTML = "application/xhtml+xml";
        public const String MIMETYPE_PDF = "application/pdf";
        public const String MIMETYPE_WORD = "application/msword";
        public const String MIMETYPE_EXCEL = "application/vnd.excel";
        public const String MIMETYPE_BINARY = "application/octet-stream";
        public const String MIMETYPE_PPT = "application/vnd.powerpoint";
        public const String MIMETYPE_FLASH = "application/x-shockwave-flash";
        public const String MIMETYPE_IMAGE_GIF = "image/gif";
        public const String MIMETYPE_IMAGE_JPEG = "image/jpeg";
        public const String MIMETYPE_IMAGE_RGB = "image/x-rgb";
        public const String MIMETYPE_JAVASCRIPT = "application/x-javascript";

        // Open Document
        public const String MIMETYPE_OPENDOCUMENT_TEXT = "application/vnd.oasis.opendocument.text";
        public const String MIMETYPE_OPENDOCUMENT_TEXT_TEMPLATE = "application/vnd.oasis.opendocument.text-template";
        public const String MIMETYPE_OPENDOCUMENT_GRAPHICS = "application/vnd.oasis.opendocument.graphics";
        public const String MIMETYPE_OPENDOCUMENT_GRAPHICS_TEMPLATE= "application/vnd.oasis.opendocument.graphics-template";
        public const String MIMETYPE_OPENDOCUMENT_PRESENTATION= "application/vnd.oasis.opendocument.presentation";
        public const String MIMETYPE_OPENDOCUMENT_PRESENTATION_TEMPLATE= "application/vnd.oasis.opendocument.presentation-template";
        public const String MIMETYPE_OPENDOCUMENT_SPREADSHEET= "application/vnd.oasis.opendocument.spreadsheet";
        public const String MIMETYPE_OPENDOCUMENT_SPREADSHEET_TEMPLATE= "application/vnd.oasis.opendocument.spreadsheet-template";
        public const String MIMETYPE_OPENDOCUMENT_CHART= "application/vnd.oasis.opendocument.chart";
        public const String MIMETYPE_OPENDOCUMENT_CHART_TEMPLATE= "applicationvnd.oasis.opendocument.chart-template";
        public const String MIMETYPE_OPENDOCUMENT_IMAGE= "application/vnd.oasis.opendocument.image";
        public const String MIMETYPE_OPENDOCUMENT_IMAGE_TEMPLATE= "applicationvnd.oasis.opendocument.image-template";
        public const String MIMETYPE_OPENDOCUMENT_FORMULA= "application/vnd.oasis.opendocument.formula";
        public const String MIMETYPE_OPENDOCUMENT_FORMULA_TEMPLATE= "applicationvnd.oasis.opendocument.formula-template";
        public const String MIMETYPE_OPENDOCUMENT_TEXT_MASTER= "application/vnd.oasis.opendocument.text-master";
        public const String MIMETYPE_OPENDOCUMENT_TEXT_WEB= "application/vnd.oasis.opendocument.text-web";
        public const String MIMETYPE_OPENDOCUMENT_DATABASE= "application/vnd.oasis.opendocument.database";

        // Open Office
        public const String MIMETYPE_OPENOFFICE1_WRITER = "application/vnd.sun.xml.writer";
        public const String MIMETYPE_OPENOFFICE1_CALC = "application/vnd.sun.xml.calc";
        public const String MIMETYPE_OPENOFFICE1_DRAW = "application/vnd.sun.xml.draw";
        public const String MIMETYPE_OPENOFFICE1_IMPRESS = "application/vnd.sun.xml.impress";

        // Star Office
        public const String MIMETYPE_STAROFFICE5_DRAW = "application/vnd.stardivision.draw";
        public const String MIMETYPE_STAROFFICE5_CALC = "application/vnd.stardivision.calc";
        public const String MIMETYPE_STAROFFICE5_IMPRESS = "application/vnd.stardivision.impress";
        public const String MIMETYPE_STAROFFICE5_IMPRESS_PACKED = "application/vnd.stardivision.impress-packed";
        public const String MIMETYPE_STAROFFICE5_CHART = "application/vnd.stardivision.chart";
        public const String MIMETYPE_STAROFFICE5_WRITER = "application/vnd.stardivision.writer";
        public const String MIMETYPE_STAROFFICE5_WRITER_GLOBAL = "application/vnd.stardivision.writer-global";
        public const String MIMETYPE_STAROFFICE5_MATH = "application/vnd.stardivision.math";

        // Audio
        public const String MIMETYPE_MP3 = "audio/x-mpeg";

        // Alfresco
        public const String MIMETYPE_ACP = "application/acp";

        /// <summary>
        /// Singleton instance
        /// </summary>
        private static MimetypeMap instance = null;

        /// <summary>
        /// Mimetype maps and list
        /// </summary>
        private List<String> mimetypes;
        private Dictionary<String, String> extensionsByMimetype;
        private Dictionary<String, String> mimetypesByExtension;
        private Dictionary<String, String> displaysByMimetype;
        private Dictionary<String, String> displaysByExtension;
        
        /// <summary>
        /// Constructor
        /// </summary>
        public MimetypeMap()
        {
            // Initialise the internal maps of mimetype details
            InitialiseMap();
        }

        /// <summary>
        /// Initialise the maps containing the mimetype information read from the configuration file
        /// </summary>
        private void InitialiseMap()
        {
            // Create the internal list and maps
            this.mimetypes = new List<string>(50);
            this.extensionsByMimetype = new Dictionary<string, string>(50);
            this.mimetypesByExtension = new Dictionary<string, string>(50);
            this.displaysByMimetype = new Dictionary<string, string>(50);
            this.displaysByExtension = new Dictionary<string, string>(50);

            XmlReader xmlReader = new XmlTextReader(System.IO.File.OpenRead(CONFIG_FILE_LOCATION));
            while (xmlReader.Read() == true)
            {
                if (xmlReader.Name == "mimetype" && xmlReader.NodeType == XmlNodeType.Element)
                {
                    // Get the details of the mimetype
                    String mimetype = xmlReader.GetAttribute("mimetype");
                    String mimetypeDisplay = xmlReader.GetAttribute("display");

                    // Add the mimetype to the list
                    this.mimetypes.Add(mimetype);

                    while (xmlReader.Read() == true)
                    {
                        string defaultExtension = null;
                        bool first = true;

                        if (xmlReader.Name == "extension" && xmlReader.NodeType == XmlNodeType.Element)
                        {
                            // Read the details of the extensions relating to the mime type
                            String extensionDisplay = xmlReader.GetAttribute("display");
                            xmlReader.Read();
                            String extension = xmlReader.Value;

                            // Sort out the default extension for the mimetype
                            if (first == true)
                            {
                                defaultExtension = extension;
                                first = false;
                            }
                            else
                            {
                                string defaultValue = xmlReader.GetAttribute("default");
                                if (defaultValue != null && defaultValue.ToLower() == "true")
                                {
                                    // Set the default value to the current extension
                                    defaultExtension = extension;
                                }
                            }

                            // Add the details to the internal maps                            
                            this.mimetypesByExtension[extension] = mimetype;
                            this.displaysByExtension[extension] = extensionDisplay;
                            this.displaysByMimetype[mimetype] = mimetypeDisplay;
                        }
                        else if (xmlReader.Name == "mimetype")
                        {
                            this.extensionsByMimetype[mimetype] = defaultExtension;
                            break;
                        }
                    }
                }
            }
        }

        /// <summary>
        /// Singleton method returning the instnace of the mimetype map
        /// </summary>
        public static MimetypeMap Instance
        {
            get
            {
                if (MimetypeMap.instance == null)
                {
                    MimetypeMap.instance = new MimetypeMap();
                }
                return MimetypeMap.instance;
            }
        }

        /// <summary>
        /// Gets file extension given a mimetype.  Returns the default value if more than one if appropraite.
        /// </summary>
        /// <param name="mimetype"></param>
        /// <returns></returns>
        public String GetExtension(String mimetype)
        {
            String extension = extensionsByMimetype[mimetype];
            if (extension == null)
            {
                throw new AlfrescoException("No extension available for mimetype: " + mimetype);
            }
            return extension;
        }

        public Dictionary<String, String> DisplaysByExtension
        {
            get
            {
                return displaysByExtension;
            }
        }

        public Dictionary<String, String> DisplaysByMimetype
        {
            get
            {
                return displaysByMimetype;
            }
        }

        public Dictionary<String, String> getExtensionsByMimetype
        {
            get
            {
                return extensionsByMimetype;
            }
        }

        public List<String> Mimetypes
        {
            get
            {
                return mimetypes;
            }
        }

        public Dictionary<String, String> MimetypesByExtension
        {
            get
            {
                return mimetypesByExtension;
            }
        }

        /// <summary>
        /// Makes a guess at the mimetype given a file name.
        /// </summary>
        /// <param name="filename"></param>
        /// <returns></returns>
        public String GuessMimetype(String filename)
        {
            filename = filename.ToLower();
            String mimetype = MIMETYPE_BINARY;
            // extract the extension
            int index = filename.LastIndexOf('.');
            if (index > -1 && (index < filename.Length - 1))
            {
                String extension = filename.Substring(index + 1);
                if (mimetypesByExtension.ContainsKey(extension))
                {
                    mimetype = mimetypesByExtension[extension];
                }
            }
            return mimetype;
        }
    }
}
