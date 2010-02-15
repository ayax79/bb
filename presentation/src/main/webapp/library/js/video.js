if(typeof bb === undefined || bb == null) var bb = {};

bb.embedFlowPlayer = function(vid) {
	flowplayer(vid, {src:'/library/swf/flowplayer.commercial-3.1.1.swf', wmode:'opaque'}, {
		plugins: {
			controls: {
				url: '/library/swf/flowplayer.controls-3.1.1.swf',
				bottom:0,
				height:24,
				backgroundColor: '#222222',
				backgroundGradient: 'none',
				progressColor:	  '#aaaaaa',
				fontColor: '#ffffff',
				bufferColor: '#666666',
				buttonOverColor: "#000000",
				timeFontColor: '#333333',
				buttonColor: '#888888',
				autoHide: 'always',
				hideDelay: 1000,
				play:true,
				volume:false,
				mute:true,
				time:false,
				stop:true,
				playlist:false,
				fullscreen:false,
				scrubber: true
			}
		},
		key: '#$3a7ccc101398214fe9b',
		play: {
			url: '/library/images/play-button.png',
			width: 96,
			height: 80
		},
		clip: {
			autoPlay:false
		},
		contextMenu: ['Blackbox Republic'],
		canvas: {
			backgroundColor:'#000000',
			backgroundGradient: 'none'
		}
	});
};