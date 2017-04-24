/** @license MIT License (c) Heyday Digital */

/**
 * An extremely minimal DOM element class manipulator
 *
 * Licensed under the MIT License at:
 * http://heyday.mit-license.org/
 *
 * @version 0.1.4
 */

/*jshint laxbreak:true, expr:true */
( function( define ) {
define( function() {

	var DOMClass, wrap, trim, EMPTY = ' ';

	/**
	 * Wrap string in whitespace
	 * @private
	 * @param  {String} str String to be wrapped
	 * @return {String}
	 */
	wrap = function( str ) {
		return EMPTY + str + EMPTY;
	};

	/**
	 * Trim additional white space off the start and end of a string
	 * @private
	 * @param  {String} str String to be trimmed
	 * @return {String}
	 */
	trim = function( str ) {
		return !str ? '' : str.toString().replace( /^\s+|\s+$/g, '' );
	};


	/**
	 * Constructor for the dom-class manipulator
	 * @class DOMClass
	 *
	 * @constructor
	 * @param {HTMLElement} el Dom node to perform class manipulations on
	 */
	DOMClass = function( el ) {
		this.e = el;
	};

	DOMClass.prototype = {

		/**
		 * Check if HTMLElement has a specific class
		 * @param  {String}  str String to be searched for in className
		 * @return {Boolean}
		 */
		has: function( str ) {
			return ( wrap( this.get() ) ).indexOf( wrap( str ) ) > -1;
		},

		/**
		 * Get className value for HTMLElement
		 * @return {String}
		 */
		get: function() {
			return trim( this.e.className );
		},

		/**
		 * Add className to HTMLElement if not already added
		 * @param {String} str class to be added
		 * @chainable
		 */
		add: function( str ) {
			if ( !this.has( str ) ) {
				this.e.className += EMPTY + str;
			}
			return this;
		},

		/**
		 * Remove className from HTMLElement
		 * @param  {String} str String to be removed
		 * @chainable
		 */
		remove: function( str ) {
			this.e.className = trim( wrap( this.get() ).replace( wrap( str ), EMPTY ) );
			return this;
		}
	};

	/**
	 * Create new className manipulator wrapping a HTMLElement
	 * @param  {HTMLElement} el Element to wrap
	 * @return {DOMClass}    DOMClass instance
	 */
	return function( el ) {
		return new DOMClass( el );
	};
} );
} )( typeof define === 'function' && define.amd
		? define
		: function( factory ) { typeof exports === 'object' ? module.exports = factory() : this.domClass = factory(); }
			// Boilerplate for AMD, CommonJS and browser global
);