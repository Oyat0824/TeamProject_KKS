<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- DOMPurify -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/dompurify/2.3.0/purify.min.js"></script>
<!-- 토스트 UI 에디터 코어 -->
<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
<link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
<!-- 토스트 UI 에디터 플러그인, 컬러피커 -->
<link rel="stylesheet" href="https://uicdn.toast.com/tui-color-picker/latest/tui-color-picker.css" />
<script src="https://uicdn.toast.com/tui-color-picker/latest/tui-color-picker.min.js"></script>
<link rel="stylesheet" href="https://uicdn.toast.com/editor-plugin-color-syntax/latest/toastui-editor-plugin-color-syntax.min.css" />
<script src="https://uicdn.toast.com/editor-plugin-color-syntax/latest/toastui-editor-plugin-color-syntax.min.js"></script>
<!-- 토스트 UI 차트 -->
<link rel="stylesheet" href="https://uicdn.toast.com/chart/latest/toastui-chart.css">
<script src="https://uicdn.toast.com/chart/latest/toastui-chart.js"></script>
<!-- 토스트 UI 차트와 토스트 UI 에디터를 연결  -->
<script src="https://uicdn.toast.com/editor-plugin-chart/latest/toastui-editor-plugin-chart.min.js"></script>
<!-- 토스트 UI 에디터 플러그인, 코드 신텍스 하이라이터 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/prism/1.24.1/themes/prism.min.css">
<link rel="stylesheet" href="https://uicdn.toast.com/editor-plugin-code-syntax-highlight/latest/toastui-editor-plugin-code-syntax-highlight.min.css">
<script src="https://uicdn.toast.com/editor-plugin-code-syntax-highlight/latest/toastui-editor-plugin-code-syntax-highlight-all.min.js"></script>
<!-- 토스트 UI 에디터 플러그인, 테이블 셀 병합 -->
<script src="https://uicdn.toast.com/editor-plugin-table-merged-cell/latest/toastui-editor-plugin-table-merged-cell.min.js"></script>
<!-- 토스트 UI 에디터 플러그인, katex -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.13.13/katex.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.13.13/katex.min.css">
<!-- 토스트 UI 에디터 플러그인, UML -->
<script src="https://uicdn.toast.com/editor-plugin-uml/latest/toastui-editor-plugin-uml.min.js"></script>

<style>
/* 토스트 UI */
.ratio-16\/9::after {
	content: "";
	display: block;
	padding-top: calc(100%/ 16 * 9);
}

.ratio-16\/9::after {
	content: "";
	display: block;
	padding-top: calc(100%/ 16 * 9);
}

.ratio-9\/16::after {
	content: "";
	display: block;
	padding-top: calc(100%/ 9 * 16);
}

.ratio-1\/1::after {
	content: "";
	display: block;
	padding-top: calc(100%/ 1 * 1);
}

.ratio-1\/2::after {
	content: "";
	display: block;
	padding-top: calc(100%/ 1 * 2);
}
</style>

<script>
function getUriParams(uri) {
	  uri = uri.trim();
	  uri = uri.replaceAll('&amp;', '&');
	  if ( uri.indexOf('#') !== -1 ) {
	    let pos = uri.indexOf('#');
	    uri = uri.substr(0, pos);
	  }

	  let params = {};

	  uri.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) { params[key] = value; });
	  return params;
	}

	function codepenPlugin() {
	  const toHTMLRenderers = {
	    codepen(node) {
	      const html = renderCodepen(node.literal);

	      return [
	        { type: 'openTag', tagName: 'div', outerNewLine: true },
	        { type: 'html', content: html },
	        { type: 'closeTag', tagName: 'div', outerNewLine: true }
	      ];
	    }
	  }
	  
	  function renderCodepen(uri) {    
	    let uriParams = getUriParams(uri);

	    let height = 400;

	    let preview = '';

	    if ( uriParams.height ) {
	      height = uriParams.height;
	    }

	    let width = '100%';

	    if ( uriParams.width ) {
	      width = uriParams.width;
	    }

	    if ( !isNaN(width) ) {
	      width += 'px';
	    }

	    let iframeUri = uri;

	    if ( iframeUri.indexOf('#') !== -1 ) {
	      let pos = iframeUri.indexOf('#');
	      iframeUri = iframeUri.substr(0, pos);
	    }

	    return '<iframe height="' + height + '" style="width: ' + width + ';" scrolling="no" title="" src="' + iframeUri + '" frameborder="no" allowtransparency="true" allowfullscreen="true"></iframe>';
	  }

	  return { toHTMLRenderers }
	}

	function replPlugin() {
	  const toHTMLRenderers = {
	    repl(node) {
	      const html = renderRepl(node.literal);

	      return [
	        { type: 'openTag', tagName: 'div', outerNewLine: true },
	        { type: 'html', content: html },
	        { type: 'closeTag', tagName: 'div', outerNewLine: true }
	      ];
	    }
	  }
	  
	  function renderRepl(uri) {
	    var uriParams = getUriParams(uri);

	    var height = 400;

	    if ( uriParams.height ) {
	      height = uriParams.height;
	    }

	    return '<iframe frameborder="0" width="100%" height="' + height +'px" src="' + uri + '"></iframe>';
	  }

	  return { toHTMLRenderers }
	}

	function youtubePlugin() {
	  const toHTMLRenderers = {
	    youtube(node) {
	      const html = renderYoutube(node.literal);

	      return [
	        { type: 'openTag', tagName: 'div', outerNewLine: true },
	        { type: 'html', content: html },
	        { type: 'closeTag', tagName: 'div', outerNewLine: true }
	      ];
	    }
	  }

	  function renderYoutube(uri) {
	    uri = uri.replace('https://www.youtube.com/watch?v=', '');
	    uri = uri.replace('http://www.youtube.com/watch?v=', '');
	    uri = uri.replace('www.youtube.com/watch?v=', '');
	    uri = uri.replace('youtube.com/watch?v=', '');
	    uri = uri.replace('https://youtu.be/', '');
	    uri = uri.replace('http://youtu.be/', '');
	    uri = uri.replace('youtu.be/', '');

	    let uriParams = getUriParams(uri);

	    let width = '100%';
	    let height = '100%';

	    let maxWidth = 500;

	    if ( !uriParams['max-width'] && uriParams['ratio'] == '9/16' ) {
	      uriParams['max-width'] = 300;
	    }

	    if ( uriParams['max-width'] ) {
	      maxWidth = uriParams['max-width'];
	    }

	    let ratio = '16/9';

	    if ( uriParams['ratio'] ) {
	      ratio = uriParams['ratio'];
	    }

	    let marginLeft = 'auto';

	    if ( uriParams['margin-left'] ) {
	      marginLeft = uriParams['margin-left'];
	    }

	    let marginRight = 'auto';

	    if ( uriParams['margin-right'] ) {
	      marginRight = uriParams['margin-right'];
	    }

	    let youtubeId = uri;

	    if ( youtubeId.indexOf('?') !== -1 ) {
	      let pos = uri.indexOf('?');
	      youtubeId = youtubeId.substr(0, pos);
	    }

	    return '<div style="max-width:' + maxWidth + 'px; margin-left:' + marginLeft + '; margin-right:' + marginRight + ';" class="ratio-' + ratio + ' relative"><iframe class="absolute top-0 left-0 w-full" width="' + width + '" height="' + height + '" src="https://www.youtube.com/embed/' + youtubeId + '" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe></div>';
	  }

	  return { toHTMLRenderers }
	}

	function katexPlugin() {
	  const toHTMLRenderers = {
	    katex(node) {
	      let html = katex.renderToString(node.literal, {
	        throwOnError: false
	      });

	      return [
	        { type: 'openTag', tagName: 'div', outerNewLine: true },
	        { type: 'html', content: html },
	        { type: 'closeTag', tagName: 'div', outerNewLine: true }
	      ];
	    },
	  }

	  return { toHTMLRenderers }
	}

	const ToastEditor__chartOptions = {
	  minWidth: 100,
	  maxWidth: 600,
	  minHeight: 100,
	  maxHeight: 300
	};

	function ToastEditor__init() {
	  $('.toast-ui-editor').each(function(index, node) {
	    const $node = $(node);
	    const $initialValueEl = $node.find(' > script');
	    const initialValue = $initialValueEl.length == 0 ? '' : $initialValueEl.html().trim();

	    const editor = new toastui.Editor({
	      el: node,
	      previewStyle: 'tab',
	      initialValue: initialValue,
	      initialEditType: 'wysiwyg',
	      height:'600px',
	      plugins: [
	        [toastui.Editor.plugin.chart, ToastEditor__chartOptions],
	        [toastui.Editor.plugin.codeSyntaxHighlight, {highlighter:Prism}],
	        toastui.Editor.plugin.colorSyntax,
	        toastui.Editor.plugin.tableMergedCell,
	        toastui.Editor.plugin.uml,
	        katexPlugin,
	        youtubePlugin,
	        codepenPlugin,
	        replPlugin
	      ],
	      customHTMLSanitizer: html => {
	        return DOMPurify.sanitize(html, { ADD_TAGS: ["iframe"], ADD_ATTR: ['width', 'height', 'allow', 'allowfullscreen', 'frameborder', 'scrolling', 'style', 'title', 'loading', 'allowtransparency'] }) || ''
	      }
	    });

	    $node.data('data-toast-editor', editor);
	  });
	}

	function ToastEditorView__init() {
	  $('.toast-ui-viewer').each(function(index, node) {
	    const $node = $(node);
	    const $initialValueEl = $node.find(' > script');
	    const initialValue = $initialValueEl.length == 0 ? '' : $initialValueEl.html().trim();
	    $node.empty();

	    let viewer = new toastui.Editor.factory({
	      el: node,
	      initialValue: initialValue,
	      viewer:true,
	      plugins: [
	        [toastui.Editor.plugin.chart, ToastEditor__chartOptions],
	        [toastui.Editor.plugin.codeSyntaxHighlight, {highlighter:Prism}],
	        toastui.Editor.plugin.colorSyntax,
	        toastui.Editor.plugin.tableMergedCell,
	        toastui.Editor.plugin.uml,
	        katexPlugin,
	        youtubePlugin,
	        codepenPlugin,
	        replPlugin
	      ],
	      customHTMLSanitizer: html => {
	        return DOMPurify.sanitize(html, { ADD_TAGS: ["iframe"], ADD_ATTR: ['width', 'height', 'allow', 'allowfullscreen', 'frameborder', 'scrolling', 'style', 'title', 'loading', 'allowtransparency'] }) || ''
	      }
	    });

	    $node.data('data-toast-editor', viewer);
	  });
	}

	$(function() {
	  ToastEditor__init();
	  ToastEditorView__init();
	});


	 const submitForm = function(form) {
	  form.title.value = form.title.value.trim();
	  
	  if(form.title.value.length == 0) {
	    alert("제목을 입력해주세요")
	    form.title.value.focus();
	    return false;
	  }
	  
	  const editor = $(form).find(".toast-ui-editor").data("data-toast-editor");
	  const markdown = editor.getMarkdown().trim();
	  const html = editor.getHTML().trim();
	
	  if(markdown.length == 0) {
	    alert("내용을 입력해주세요");
	    editor.focus();
	    return false;
	  }
	  
	  form.body.value = html;
	  
	  form.submit();
	}
</script>