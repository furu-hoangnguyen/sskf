const getBrowserName = () => {
var isOpera = (!!window.opr && !!window.opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
// Firefox 1.0+
var isFirefox = typeof InstallTrigger !== 'undefined';
// Safari 3.0+ "[object HTMLElementConstructor]" 

var isSafari = navigator.vendor && navigator.vendor.indexOf('Apple') > -1 &&
              navigator.userAgent &&
              navigator.userAgent.indexOf('CriOS') == -1 &&
              navigator.userAgent.indexOf('FxiOS') == -1;
// Internet Explorer 6-11
var isIE = /*@cc_on!@*/false || !!document.documentMode;
// Edge 20+
var isEdge = !isIE && !!window.StyleMedia;
// Chrome 1 - 79
var isChrome = !!window.chrome && (!!window.chrome.webstore || !!window.chrome.runtime) || navigator.userAgent.indexOf("Chrome") > -1;
// Edge (based on chromium) detection
var isEdgeChromium = isChrome && (navigator.userAgent.indexOf("Edg") != -1);
// Blink engine detection
var isBlink = (isChrome || isOpera) && !!window.CSS;

return isOpera ? "Opera" : isFirefox ? "Firefox" : isChrome ? "Chrome" : isSafari ? "Safari" : isEdge ? "Edge" : isIE ? "IE" : isEdgeChromium ? "EdgeChromium" : isBlink ? "Blink" : "warning";
}
export default getBrowserName;