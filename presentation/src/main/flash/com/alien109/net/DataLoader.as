package com.alien109.net
{
	import flash.events.*;
	import flash.net.URLRequest;
	import flash.net.URLLoader;
	import flash.events.EventDispatcher;
	
	import com.alien109.net.DataReadyEvent;

	public class DataLoader extends EventDispatcher
	{
		public function DataLoader(){}
		public function loadData(path:String):void
		{
			var req:URLRequest=new URLRequest(path);
			var loader:URLLoader=new URLLoader(req);
			loader.addEventListener(Event.COMPLETE,loadComplete);
			loader.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
			loader.addEventListener(HTTPStatusEvent.HTTP_STATUS, httpStatusHandler);			
		}
		
		private function loadComplete(event:Event):void
		{
			var loader:URLLoader=URLLoader(event.target);
			dispatchEvent(new DataReadyEvent(DataReadyEvent.LOADED, event.target.data))
		}
		
		private function ioErrorHandler(event:IOErrorEvent):void
		{
			
			dispatchEvent(new IOErrorEvent(IOErrorEvent.IO_ERROR, true, false, event.text));
		}
		
		private function httpStatusHandler(event:HTTPStatusEvent):void
		{
			dispatchEvent(new HTTPStatusEvent(HTTPStatusEvent.HTTP_STATUS, true, false, event.status));
		}

	}
}