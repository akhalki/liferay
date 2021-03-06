/*
Copyright (c) 2010, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.com/yui/license.html
version: 3.2.0
build: nightly
*/
YUI.add("querystring-stringify",function(D){var C=D.namespace("QueryString"),B=[],A=D.Lang;C.escape=encodeURIComponent;C.stringify=function(J,L,E){var G,I,K,H,F,P,O=L&&L.sep?L.sep:"&",M=L&&L.eq?L.eq:"=",N=L&&L.arrayKey?L.arrayKey:false;if(A.isNull(J)||A.isUndefined(J)||A.isFunction(J)){return E?C.escape(E)+M:"";}if(A.isBoolean(J)||Object.prototype.toString.call(J)==="[object Boolean]"){J=+J;}if(A.isNumber(J)||A.isString(J)){return C.escape(E)+M+C.escape(J);}if(A.isArray(J)){P=[];E=N?E+"[]":E;H=J.length;for(K=0;K<H;K++){P.push(C.stringify(J[K],L,E));}return P.join(O);}for(K=B.length-1;K>=0;--K){if(B[K]===J){throw new Error("QueryString.stringify. Cyclical reference");}}B.push(J);P=[];G=E?E+"[":"";I=E?"]":"";for(K in J){if(J.hasOwnProperty(K)){F=G+K+I;P.push(C.stringify(J[K],L,F));}}B.pop();P=P.join(O);if(!P&&E){return E+"=";}return P;};},"3.2.0");