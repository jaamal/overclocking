if (!Array.prototype.indexOf) {
	Array.prototype.indexOf = function(element) {
		for (var i = 0; i < this.length; i++) {
			if (this[i] == element)
				return i;
		}
		return -1;
	};
}