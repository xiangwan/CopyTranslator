using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation; 
using Windows.Data.Xml.Dom;
using Windows.UI.Notifications;
using CopyTranslator.Code;
using CopyTranslator.Code.Translator;

namespace CopyTranslator
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private const string APP_ID = "XiangWan.CopyTranslator";
        private readonly  ITranslatorProvider translator=new BaiduTranslatorProvider();
        public MainWindow()
        {
            InitializeComponent();
            ShortcutHelper.TryCreateShortcut(APP_ID); 
        }

        private async void ClipboardMonitor_OnClipboardChange(ClipboardFormat format, object data)
        {
            if (App.LastBoradData==data)
            {
                return;
            }
            App.LastBoradData = data;
            switch (format)
            {
                case ClipboardFormat.Text:
                    await Task.Factory.StartNew(async () =>
                    {
                        var dict = await translator.Translator(data.ToString());
                        var firstKey=   dict.Keys.FirstOrDefault();
                        var firstValue = dict.Values.FirstOrDefault();
                        if (firstKey != null)
                        {
                            var msg = string.Format("{0} --> {1}", firstKey, firstValue ?? "翻译失败");
                            CreateToast(data.ToString(), msg, data.ToString(), "power by xiangwan");
                        } 
                    });
                    break;
            }
        }

        private void CreateToast(string data,string line1 ,string line2 ,string line3)
        { 
            // Get a toast XML template
            XmlDocument toastXml = ToastNotificationManager.GetTemplateContent(ToastTemplateType.ToastText02);

            // Fill in the text elements
            XmlNodeList stringElements = toastXml.GetElementsByTagName("text");
            
            stringElements[0].AppendChild(toastXml.CreateTextNode(line1));
           // stringElements[1].AppendChild(toastXml.CreateTextNode(line2));
            //stringElements[2].AppendChild(toastXml.CreateTextNode(line3));  
           
              IXmlNode toastNode = toastXml.SelectSingleNode("/toast");
              ((XmlElement)toastNode).SetAttribute("duration", "3000");
              ((XmlElement)toastNode).SetAttribute("data", data);
            var toast = new ToastNotification(toastXml);
            toast.Activated +=toast_Activated;            
            // Show the toast. Be sure to specify the AppUserModelId on your application's shortcut!
            ToastNotificationManager.CreateToastNotifier(APP_ID).Show(toast);
        }
         

        private void toast_Activated(ToastNotification sender, object e)
        {
            var data = sender.Content.SelectSingleNode("/toast").Attributes.GetNamedItem("data").InnerText;
           Process.Start("http://www.bing.com/dict/search?intlF=0&q=" + data);
        }

   
         

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            ClipboardMonitor.Stop(); 
        }

        private void ButtonStart_OnClick(object sender, RoutedEventArgs e)
        {
            ClipboardMonitor.Start();
            ClipboardMonitor.OnClipboardChange += ClipboardMonitor_OnClipboardChange; 
        }

        private void ButtonStop_OnClick(object sender, RoutedEventArgs e)
        {
            ClipboardMonitor.Stop(); 
        }
    }
}
