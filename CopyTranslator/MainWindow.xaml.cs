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
using Ivony.Logs;

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
            App.Logger.LogInfo("******主程序启动******");
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
                    App.Logger.LogInfo("剪切板当前值{0}", data);
                    await Task.Factory.StartNew(async () =>
                    {
                        var dict = await translator.Translator(data.ToString());
                        var firstKey=   dict.Keys.FirstOrDefault();
                        var firstValue = dict.Values.FirstOrDefault();
                        if (firstKey != null)
                        {
                            var msg = string.Format("{0} --> {1}", firstKey, firstValue ?? "翻译失败");
                            CreateToast(data.ToString(), msg, "点击用浏览器搜索", "power by xiangwan");
                        }
                        else
                        {
                            App.Logger.LogInfo("网络返回空值");
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
            //stringElements[1].AppendChild(toastXml.CreateTextNode(line2));
            //stringElements[2].AppendChild(toastXml.CreateTextNode(line3));  
           
             
            var toast = new ToastNotification(toastXml);
            toast.Activated += (s, e) =>
            {
                var text = toastXml.GetElementsByTagName("text")[0].ChildNodes[0].InnerText;
                var word = text.Substring(0, text.IndexOf("-->"));
                Process.Start("http://www.bing.com/dict/search?intlF=0&q=" + word);
                App.Logger.LogInfo("toast_Activated，已打开浏览器搜索");
            };
            toast.Dismissed += (s, e) => App.Logger.LogInfo("toast Dismissed ");
            toast.Failed += (s, e) => App.Logger.LogWarning("toast.Failed : {0}", e.ErrorCode);
            // Show the toast. Be sure to specify the AppUserModelId on your application's shortcut!
            ToastNotificationManager.CreateToastNotifier(APP_ID).Show(toast);
            App.Logger.LogInfo("已成功创建TOAST");
        }
         
 
   
         

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            ClipboardMonitor.Stop();
            App.Logger.LogInfo("******主程序关闭******");
        }

        private void ButtonStart_OnClick(object sender, RoutedEventArgs e)
        {
            Clipboard.Clear();
            ClipboardMonitor.Start();
            ClipboardMonitor.OnClipboardChange += ClipboardMonitor_OnClipboardChange;
            App.Logger.LogInfo("======启动剪切板监控======");
            CreateToast("向晚","提示 --> 点击消息会打开浏览器搜索","","");
        }

        private void ButtonStop_OnClick(object sender, RoutedEventArgs e)
        {
            ClipboardMonitor.Stop();
            App.Logger.LogInfo("======关闭剪切板监控======");
        }
    }
}
