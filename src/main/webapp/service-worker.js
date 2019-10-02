"use strict";var precacheConfig=[["/balcaovirtual/index.html","6ea4f1932d90bcced38b62a7eb0c7caa"],["/balcaovirtual/manifest.fbf00d495ba557ac8b6dc512735c9d3f.json","fbf00d495ba557ac8b6dc512735c9d3f"],["/balcaovirtual/static/css/app.cfda10ed0a0aa6b9780aded17a415d79.css","52da0d78a1c9b3c7ba1dc1debd8be692"],["/balcaovirtual/static/favicon-16x16.png","e16c996dd8cace7223b9807599d63546"],["/balcaovirtual/static/favicon-32x32.png","675e43cefd931d5c1c6e8829949141d0"],["/balcaovirtual/static/favicon-96x96.png","63ca5aed46bf9328cf18f51cadafaa1c"],["/balcaovirtual/static/fonts/fontawesome-webfont.674f50d.eot","674f50d287a8c48dc19ba404d20fe713"],["/balcaovirtual/static/fonts/fontawesome-webfont.af7ae50.woff2","af7ae505a9eed503f8b8e6982036873e"],["/balcaovirtual/static/fonts/fontawesome-webfont.b06871f.ttf","b06871f281fee6b241d60582ae9369b9"],["/balcaovirtual/static/fonts/fontawesome-webfont.fee66e7.woff","fee66e712a8a08eef5805a46892932ad"],["/balcaovirtual/static/icons/icon_128x128.aa2d34248c9a8beb0b201107e8d0d9cd.png","aa2d34248c9a8beb0b201107e8d0d9cd"],["/balcaovirtual/static/icons/icon_192x192.45a00d564ea8d7bf8640c81c3cd34f40.png","45a00d564ea8d7bf8640c81c3cd34f40"],["/balcaovirtual/static/icons/icon_256x256.c9f89dccd3074bfc2f988a78ceec71bd.png","c9f89dccd3074bfc2f988a78ceec71bd"],["/balcaovirtual/static/icons/icon_384x384.f6089f74ae314cba3e47deb73cec031e.png","f6089f74ae314cba3e47deb73cec031e"],["/balcaovirtual/static/icons/icon_512x512.fb754f2a98a4f06c21d8178f11949340.png","fb754f2a98a4f06c21d8178f11949340"],["/balcaovirtual/static/icons/icon_96x96.bf63c29177172a2ddc389430ed2b966f.png","bf63c29177172a2ddc389430ed2b966f"],["/balcaovirtual/static/img/fontawesome-webfont.912ec66.svg","912ec66d7572ff821749319396470bde"],["/balcaovirtual/static/img/judicia.2a740c7.png","2a740c74a2437f14d52de7f6dfa64b87"],["/balcaovirtual/static/img/users.00ce4bf.png","00ce4bf4905f0213075b365bfa263696"],["/balcaovirtual/static/js/app.48a9c4503a02aaa62fa9.js","0ab65f003525ed11e4262a7c8f0bab21"],["/balcaovirtual/static/js/manifest.630e95cbd0dd493c1888.js","626052c2ebff19835299dcd568b0d997"],["/balcaovirtual/static/js/vendor.c76afc567614e4b310b1.js","458f8a0d1feacabf6d1cc1896342d2b5"]],cacheName="sw-precache-v3-bv-cache-id-"+(self.registration?self.registration.scope:""),ignoreUrlParametersMatching=[/^utm_/],addDirectoryIndex=function(e,a){var t=new URL(e);return"/"===t.pathname.slice(-1)&&(t.pathname+=a),t.toString()},cleanResponse=function(e){return e.redirected?("body"in e?Promise.resolve(e.body):e.blob()).then(function(a){return new Response(a,{headers:e.headers,status:e.status,statusText:e.statusText})}):Promise.resolve(e)},createCacheKey=function(e,a,t,c){var n=new URL(e);return c&&n.pathname.match(c)||(n.search+=(n.search?"&":"")+encodeURIComponent(a)+"="+encodeURIComponent(t)),n.toString()},isPathWhitelisted=function(e,a){if(0===e.length)return!0;var t=new URL(a).pathname;return e.some(function(e){return t.match(e)})},stripIgnoredUrlParameters=function(e,a){var t=new URL(e);return t.hash="",t.search=t.search.slice(1).split("&").map(function(e){return e.split("=")}).filter(function(e){return a.every(function(a){return!a.test(e[0])})}).map(function(e){return e.join("=")}).join("&"),t.toString()},hashParamName="_sw-precache",urlsToCacheKeys=new Map(precacheConfig.map(function(e){var a=e[0],t=e[1],c=new URL(a,self.location),n=createCacheKey(c,hashParamName,t,/\.\w{8}\./);return[c.toString(),n]}));function setOfCachedUrls(e){return e.keys().then(function(e){return e.map(function(e){return e.url})}).then(function(e){return new Set(e)})}self.addEventListener("install",function(e){e.waitUntil(caches.open(cacheName).then(function(e){return setOfCachedUrls(e).then(function(a){return Promise.all(Array.from(urlsToCacheKeys.values()).map(function(t){if(!a.has(t)){var c=new Request(t,{credentials:"same-origin"});return fetch(c).then(function(a){if(!a.ok)throw new Error("Request for "+t+" returned a response with status "+a.status);return cleanResponse(a).then(function(a){return e.put(t,a)})})}}))})}).then(function(){return self.skipWaiting()}))}),self.addEventListener("activate",function(e){var a=new Set(urlsToCacheKeys.values());e.waitUntil(caches.open(cacheName).then(function(e){return e.keys().then(function(t){return Promise.all(t.map(function(t){if(!a.has(t.url))return e.delete(t)}))})}).then(function(){return self.clients.claim()}))}),self.addEventListener("fetch",function(e){if("GET"===e.request.method){var a,t=stripIgnoredUrlParameters(e.request.url,ignoreUrlParametersMatching);(a=urlsToCacheKeys.has(t))||(t=addDirectoryIndex(t,"index.html"),a=urlsToCacheKeys.has(t));!a&&"navigate"===e.request.mode&&isPathWhitelisted(["^index.html"],e.request.url)&&(t=new URL("/balcaovirtual/index.html",self.location).toString(),a=urlsToCacheKeys.has(t)),a&&e.respondWith(caches.open(cacheName).then(function(e){return e.match(urlsToCacheKeys.get(t)).then(function(e){if(e)return e;throw Error("The cached response that was expected is missing.")})}).catch(function(a){return console.warn('Couldn\'t serve response for "%s" from cache: %O',e.request.url,a),fetch(e.request)}))}});