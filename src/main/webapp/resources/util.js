if (typeof String.prototype.endsWith !== 'function') {
	String.prototype.endsWith = function(suffix) {
		return this.indexOf(suffix, this.length - suffix.length) !== -1;
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
	return s.split('-').join('').split('.').join('');
}

const regexFormatarProcesso = /^(\d{7})-?(\d{2})\.?(\d{4})\.?(4)\.?(02)\.?(\d{4})(\d{2})?/;

var formatarProcesso = function(filename) {
	var m = regexFormatarProcesso.exec(filename);
	if (!m)
		return;
	var s = m[1] + '-' + m[2] + '.' + m[3] + '.' + m[4]
			+ '.' + m[5] + '.' + m[6];
	if (m[7])
		s += m[7];
	return s;
}
