package com.alien109.net
{
	import flash.events.Event;
	public class DataReadyEvent extends Event
	{
		public static  const LOADED:String="loaded";
		public var data:String;
		
		public function DataReadyEvent(type:String, data:String)
		{
			super(type,true);
			this.data = data; 
		}
		public override  function clone():Event
		{
			return new DataReadyEvent(type, data);
		}
	}
}