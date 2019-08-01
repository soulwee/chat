$(function() {

	var E = window.wangEditor
	var editor = new E('#div1', '#div2'); // 两个参数也可以传入 elem 对象，class 选择器
	// 自定义菜单配置
	editor.customConfig.menus = [ 
	'head', // 标题
	'bold', // 粗体
	'fontSize', // 字号
	'fontName', // 字体
	'italic', // 斜体
	'underline', // 下划线
	'strikeThrough', // 删除线
	'foreColor', // 文字颜色
	'backColor', // 背景颜色
	'justify', // 对齐方式
	'quote', // 引用
	'emoticon', // 表情
	'image' // 插入图片
	];
	editor.customConfig.uploadImgServer = 'upload';
	//editor.customConfig.uploadImgShowBase64 = true;
	editor.customConfig.emotions = [
			{
				// tab 的标题
				title : '表情包',
				// type -> 'emoji' / 'image'
				type : 'image',
				// content -> 数组
				content : [
						{
							alt : '1',
							src : 'image/zhangxueyou/0.jpg'
						}]
			},{
				// tab 的标题
	            title: 'emoji',
	            // type -> 'emoji' / 'image'
	            type: 'emoji',
	            // content -> 数组
	            content: ['😀', '😃', '😄', '😁', '😆']
			} ];

	editor.create();
});
$(function(){

	$(".w-e-emoticon-container .w-e-item img").css({width:"50px",height:"50px;"});
});
