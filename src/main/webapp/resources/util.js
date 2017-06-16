if (typeof String.prototype.endsWith !== 'function') {
	String.prototype.endsWith = function(suffix) {
		return this.indexOf(suffix, this.length - suffix.length) !== -1;
	};
}

if (typeof String.prototype.trunc !== 'function') {
	String.prototype.trunc = function( n, useWordBoundary ){
        if (this.length <= n) { return this; }
        var subString = this.substr(0, n-1);
        return (useWordBoundary 
           ? subString.substr(0, subString.lastIndexOf(' ')) 
           : subString) + "&hellip;";
     };
}

if (!Array.prototype.filter) {
	Array.prototype.filter = function(fun /* , thisp */) {
		var len = this.length >>> 0;
		if (typeof fun != "function")
			throw new TypeError();

		var res = [];
		var thisp = arguments[1];
		for (var i = 0; i < len; i++) {
			if (i in this) {
				var val = this[i]; // in case fun mutates
				// this
				if (fun.call(thisp, val, i, this))
					res.push(val);
			}
		}
		return res;
	};
}

// https://tc39.github.io/ecma262/#sec-array.prototype.includes
if (!Array.prototype.includes) {
  Object.defineProperty(Array.prototype, 'includes', {
    value: function(searchElement, fromIndex) {

      // 1. Let O be ? ToObject(this value).
      if (this == null) {
        throw new TypeError('"this" is null or not defined');
      }

      var o = Object(this);

      // 2. Let len be ? ToLength(? Get(O, "length")).
      var len = o.length >>> 0;

      // 3. If len is 0, return false.
      if (len === 0) {
        return false;
      }

      // 4. Let n be ? ToInteger(fromIndex).
      // (If fromIndex is undefined, this step produces the value 0.)
      var n = fromIndex | 0;

      // 5. If n ≥ 0, then
      // a. Let k be n.
      // 6. Else n < 0,
      // a. Let k be len + n.
      // b. If k < 0, let k be 0.
      var k = Math.max(n >= 0 ? n : len - Math.abs(n), 0);

      function sameValueZero(x, y) {
        return x === y || (typeof x === 'number' && typeof y === 'number' && isNaN(x) && isNaN(y));
      }

      // 7. Repeat, while k < len
      while (k < len) {
        // a. Let elementK be the result of ? Get(O, ! ToString(k)).
        // b. If SameValueZero(searchElement, elementK) is true, return true.
        // c. Increase k by 1.
        if (sameValueZero(o[k], searchElement)) {
          return true;
        }
        k++;
      }

      // 8. Return false
      return false;
    }
  });
}

Array.prototype.move = function (old_index, new_index) {
    if (new_index >= this.length) {
        var k = new_index - this.length;
        while ((k--) + 1) {
            this.push(undefined);
        }
    }
    this.splice(new_index, 0, this.splice(old_index, 1)[0]);
    return this; // for testing purposes
};

Number.prototype.formatMoney = function (c, d, t) {
    var n = this,
        c = isNaN(c = Math.abs(c)) ? 2 : c,
        d = d == undefined ? "." : d,
        t = t == undefined ? "," : t,
        s = n < 0 ? "-" : "",
        i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;
    return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
};

if (typeof Array.isArray === 'undefined') {
	  Array.isArray = function(obj) {
	    return Object.prototype.toString.call(obj) === '[object Array]';
	  }
	};

var formdata = function(obj) {
	var s = "";
	var f = function(prefix, jsonObj) {
		if (typeof jsonObj == "object") {
			$.each(jsonObj, function(k, v) {
				if (k == "$$hashKey")
					return;
				if (typeof v == "object") {
					var nextprefix = prefix;
					if (typeof k == "number") {
						if (prefix.endsWith(".")) {
							nextprefix = nextprefix.substring(0,
									nextprefix.length - 1);
						}
						nextprefix += "[" + k + "].";
					} else
						nextprefix += k + ".";
					f(nextprefix, v);
				} else {
					if (s != "")
						s = s + "&";
					s = s + prefix + k + "=" + v;
				}
			});
		}
	}

	f("", obj);

	return s;
}

var somenteNumeros = function(s) {
	return s.split('-').join('').split('.').join('').split('/').join('');
}

const regexFormatarProcesso = /^(\d{7})-?(\d{2})\.?(\d{4})\.?(4)\.?(02)\.?(\d{4})\/?(\d{2})?/;

var formatarProcesso = function(filename) {
	var m = regexFormatarProcesso.exec(filename);
	if (!m)
		return;
	var s = m[1] + '-' + m[2] + '.' + m[3] + '.' + m[4]
			+ '.' + m[5] + '.' + m[6];
	if (m[7])
		s += '/' + m[7];
	return s;
}

var formatarTexto = function(s) {
	return s.replace(/^\s\s*/, '').replace(/\s\s*$/, '').replace(
			/\n\s+\n/g, '<div class="break"></div>')
			.replace(/\n/g, '<br/>')
}

var arrayToString = function(a) {
	if (!Array.isArray(a))
		return a;
	var str = a.join(', ');
	var n = str.lastIndexOf(', ');
	if (n >= 0) {
	    str = str.substring(0, n) + " e " + str.substring(n+2);
	}
	return str;
}

var logEvento = function(categoria, acao, rotulo, valor) {
	try {
		ga('send', 'event', categoria, acao, rotulo, valor);
	} catch (ex) {
		
	}
}

var absoluteUrl = function (base, relative) {
    var stack = base.split("/"),
        parts = relative.split("/");
    stack.pop(); // remove current file name (or empty string)
                 // (omit if "base" is the current folder without trailing
					// slash)
    for (var i=0; i<parts.length; i++) {
        if (parts[i] == ".")
            continue;
        if (parts[i] == "..")
            stack.pop();
        else
            stack.push(parts[i]);
    }
    return stack.join("/");
}