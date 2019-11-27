// copyright 2010 Laura Shaffer Mills

var gaoTags = new Array;		// array of tag objects to count


// set up "onload" to initialize
window.onload = function() {
	if (document.getElementById) {
		document.getElementById("inputFile").onclick = countTags;
		initializeTags();		// set up search patterns
	}
}			// onload()


// count tags
function countTags() {
	// regular expression pieces for tags "<tagname(optional other stuff)>" and "</tagname>"
	var kTagOpenStart = "<";	// open, tag/endOfWord, opt. whitespace and something, close
	var kTagOpenEnd = "\\b([\\S\\s]*?)>";		// this doesn't find some self-closing tags
	var kTagOpenFlags = "gim";
	var kTagCloseStart = "<\\/";
	var kTagCloseEnd = ">";
	var kTagCloseFlags = "gi";
	var kTagSelfCloseEnd = "\\/([\\s]*?)>$";	// end of self-closing tag (slash, opt. whitespace, end
	// regular expression for comments and <script>
	var kRegExpComment = /<!--([\S\s]*?)-->/gim;		// HTML comment; global, insensitive, multiline
	var kRegExpScript = /<script([\S\s]*?)>([\S\s]*?)<\/script>/gim;	// <script>..</script> tags
	// other variables
	var cleanText;			// remove comments and <script> before counting tags
	var counter;			// loop through tag list
	var myMatches;			// tag matches
	var myText = document.HTMLContentForm.inFileContents.value;

	//	/<\/?\w+((\s+\w+(\s*=\s*(?:".*?"|'.*?'|[^'">\s]+))?)+\s*|\s*)\/?>/gim
	//	</?\w+((\s+\w+(\s*=\s*(?:".*?"|'.*?'|[^'">\s]+))?)+\s*|\s*)/?>
	//	<p[^>]*>([\S\s]*?)<\/p>

	// clear the tag counters
	for (counter = 0; counter < gaoTags.length; counter++) {
		gaoTags[counter].startCount = 0;
		gaoTags[counter].endCount = 0;
	}

	// first must remove all <script> to </script>
	cleanText = myText.replace(kRegExpScript, "");
	// then strip out HTML comments - they may contain tags that should not be counted
	cleanText = cleanText.replace(kRegExpComment, "");

	// loop through list of HTML tags
	for (counter = 0; counter < gaoTags.length; counter++) {
		var pattern;

		// open tags
		pattern = new RegExp(kTagOpenStart + gaoTags[counter].tag + kTagOpenEnd,
			kTagOpenFlags);
		myMatches = cleanText.match(pattern);
		if (myMatches != null)
			gaoTags[counter].startCount += myMatches.length;

		// find self-close tags, starting from "open" tag list
		if (myMatches != null) {	// have list of "open" tags; look for close within
			pattern = new RegExp(kTagSelfCloseEnd, kTagCloseFlags);
			for (var j = 0; j < myMatches.length; j++) {
				if ( (myMatches[j].match(pattern)) != null ) {
					gaoTags[counter].endCount += 1;
				}
			}
		}

/*		// remove open tags
		pattern = new RegExp(kTagOpenStart + gaoTags[counter].tag + kTagOpenEnd,
			kTagOpenFlags);
		cleanText = cleanText.replace(pattern, "");
*/

		// close tags
		pattern = new RegExp(kTagCloseStart + gaoTags[counter].tag + kTagCloseEnd,
			kTagCloseFlags);
		myMatches = cleanText.match(pattern);
		if (myMatches != null)
			gaoTags[counter].endCount += myMatches.length;

/*		 remove close tags
		cleanText = cleanText.replace(pattern, "");		// remove close tags
*/
	}

	removeChildNodes("outHTMLTable");	// clear old table
	displayTagTable("outHTMLTable");	// create new table
}			// countTags()


// creates one row of table (three cells) with DOM
function createRow(one, two, three, isHeader, createHighlight) {
	var nodeRow;
	var nodeCell1, nodeCell2, nodeCell3;
	var nodeText1, nodeText2, nodeText3;
	var cellType;

	if (isHeader)
		cellType = "th";
	else
		cellType = "td";

	// create nodes
	nodeRow = document.createElement("tr");				// row
	nodeCell1 = document.createElement(cellType);		// cells
	nodeCell2 = document.createElement(cellType);
	nodeCell3 = document.createElement(cellType);
	nodeText1 = document.createTextNode(one);			// text
	nodeText2 = document.createTextNode(two);
	nodeText3 = document.createTextNode(three);

	if (createHighlight) {				// change cell class to highlight
		nodeCell1.className = "highlight";
		nodeCell2.className = "highlight";
		nodeCell3.className = "highlight";
	}

	nodeRow.appendChild(nodeCell1);					// add cells to row
	nodeRow.appendChild(nodeCell2);
	nodeRow.appendChild(nodeCell3);
	nodeCell1.appendChild(nodeText1);				// add text to cells
	nodeCell2.appendChild(nodeText2);
	nodeCell3.appendChild(nodeText3);

	return(nodeRow);
}			// createRow()


// create HTML table with results
function displayTagTable(nodeParentID) {
	var parentNode = document.getElementById(nodeParentID);
	var counter;

	// header row
	parentNode.appendChild(createRow("Tag Name", "Open", "Close", true, false));

	for (counter = 0; counter < gaoTags.length; counter++) {
		var myHighlight = false;

		if ( (gaoTags[counter].startCount > 0) || (gaoTags[counter].endCount > 0) ||
			(gaoTags[counter].once == true) ) {
			if (gaoTags[counter].startCount != gaoTags[counter].endCount)
				myHighlight = true;
			if ( (gaoTags[counter].once == true) && (gaoTags[counter].startCount != 1) )
				myHighlight = true;
			parentNode.appendChild(createRow(gaoTags[counter].tag,
				gaoTags[counter].startCount, gaoTags[counter].endCount, false, myHighlight));
		}
	}
}			// displayTagTable()


// constructor for HTML tag object
function HTMLTagObj(tag, required) {
	this.tag = tag;			// HTML tag
	this.once = required;	// whether tag is required exactly once
	this.startCount = 0;
	this.endCount = 0;
}			// HTMLTag()


// initialize tag array
function initializeTags() {
	var counter = 0;

	// initialize tag list
	gaoTags[counter++] = new HTMLTagObj("a", false);
	gaoTags[counter++] = new HTMLTagObj("abbr", false);
	gaoTags[counter++] = new HTMLTagObj("acronym", false);
	gaoTags[counter++] = new HTMLTagObj("address", false);
	gaoTags[counter++] = new HTMLTagObj("applet", false);		// obsolete
	gaoTags[counter++] = new HTMLTagObj("area", false);
	gaoTags[counter++] = new HTMLTagObj("article", false);
	gaoTags[counter++] = new HTMLTagObj("aside", false);
	gaoTags[counter++] = new HTMLTagObj("audio", false);
	gaoTags[counter++] = new HTMLTagObj("b", false);
	gaoTags[counter++] = new HTMLTagObj("base", false);
	gaoTags[counter++] = new HTMLTagObj("basefont", false);		// obsolete
	gaoTags[counter++] = new HTMLTagObj("bdo", false);
	gaoTags[counter++] = new HTMLTagObj("big", false);
	gaoTags[counter++] = new HTMLTagObj("blink", false);		// obsolete
	gaoTags[counter++] = new HTMLTagObj("blockquote", false);
	gaoTags[counter++] = new HTMLTagObj("body", true);
	gaoTags[counter++] = new HTMLTagObj("br", false);
	gaoTags[counter++] = new HTMLTagObj("button", false);
	gaoTags[counter++] = new HTMLTagObj("canvas", false);
	gaoTags[counter++] = new HTMLTagObj("caption", false);
	gaoTags[counter++] = new HTMLTagObj("center", false);		// obsolete
	gaoTags[counter++] = new HTMLTagObj("cite", false);
	gaoTags[counter++] = new HTMLTagObj("code", false);
	gaoTags[counter++] = new HTMLTagObj("col", false);
	gaoTags[counter++] = new HTMLTagObj("colgroup", false);
	gaoTags[counter++] = new HTMLTagObj("command", false);
	gaoTags[counter++] = new HTMLTagObj("datalist", false);
	gaoTags[counter++] = new HTMLTagObj("dd", false);
	gaoTags[counter++] = new HTMLTagObj("del", false);
	gaoTags[counter++] = new HTMLTagObj("details", false);
	gaoTags[counter++] = new HTMLTagObj("dialog", false);
	gaoTags[counter++] = new HTMLTagObj("dfn", false);
	gaoTags[counter++] = new HTMLTagObj("dir", false);			// obsolete
	gaoTags[counter++] = new HTMLTagObj("div", false);
	gaoTags[counter++] = new HTMLTagObj("dl", false);
	gaoTags[counter++] = new HTMLTagObj("dt", false);
	gaoTags[counter++] = new HTMLTagObj("em", false);
	gaoTags[counter++] = new HTMLTagObj("embed", false);
	gaoTags[counter++] = new HTMLTagObj("fieldset", false);
	gaoTags[counter++] = new HTMLTagObj("figure", false);
	gaoTags[counter++] = new HTMLTagObj("font", false);			// obsolete
	gaoTags[counter++] = new HTMLTagObj("footer", false);
	gaoTags[counter++] = new HTMLTagObj("form", false);
	gaoTags[counter++] = new HTMLTagObj("frame", false);
	gaoTags[counter++] = new HTMLTagObj("frameset", false);
	gaoTags[counter++] = new HTMLTagObj("h1", false);
	gaoTags[counter++] = new HTMLTagObj("h2", false);
	gaoTags[counter++] = new HTMLTagObj("h3", false);
	gaoTags[counter++] = new HTMLTagObj("h4", false);
	gaoTags[counter++] = new HTMLTagObj("h5", false);
	gaoTags[counter++] = new HTMLTagObj("h6", false);
	gaoTags[counter++] = new HTMLTagObj("head", true);
	gaoTags[counter++] = new HTMLTagObj("header", false);
	gaoTags[counter++] = new HTMLTagObj("hgroup", false);
	gaoTags[counter++] = new HTMLTagObj("hr", false);
	gaoTags[counter++] = new HTMLTagObj("html", true);
	gaoTags[counter++] = new HTMLTagObj("i", false);			// obsolete
	gaoTags[counter++] = new HTMLTagObj("iframe", false);
	gaoTags[counter++] = new HTMLTagObj("img", false);
	gaoTags[counter++] = new HTMLTagObj("input", false);
	gaoTags[counter++] = new HTMLTagObj("ins", false);
	gaoTags[counter++] = new HTMLTagObj("isindex", false);		// obsolete
	gaoTags[counter++] = new HTMLTagObj("keygen", false);
	gaoTags[counter++] = new HTMLTagObj("kbd", false);
	gaoTags[counter++] = new HTMLTagObj("label", false);
	gaoTags[counter++] = new HTMLTagObj("legend", false);
	gaoTags[counter++] = new HTMLTagObj("li", false);
	gaoTags[counter++] = new HTMLTagObj("link", false);
	gaoTags[counter++] = new HTMLTagObj("map", false);
	gaoTags[counter++] = new HTMLTagObj("mark", false);
	gaoTags[counter++] = new HTMLTagObj("marquee", false);		// obsolete
	gaoTags[counter++] = new HTMLTagObj("menu", false);			// obsolete
	gaoTags[counter++] = new HTMLTagObj("meta", false);
	gaoTags[counter++] = new HTMLTagObj("meter", false);
	gaoTags[counter++] = new HTMLTagObj("nav", false);
	gaoTags[counter++] = new HTMLTagObj("nobr", false);			// obsolete
	gaoTags[counter++] = new HTMLTagObj("noembed", false);		// obsolete
	gaoTags[counter++] = new HTMLTagObj("noframes", false);
	gaoTags[counter++] = new HTMLTagObj("noscript", false);
	gaoTags[counter++] = new HTMLTagObj("object", false);
	gaoTags[counter++] = new HTMLTagObj("ol", false);
	gaoTags[counter++] = new HTMLTagObj("optgroup", false);
	gaoTags[counter++] = new HTMLTagObj("option", false);
	gaoTags[counter++] = new HTMLTagObj("output", false);
	gaoTags[counter++] = new HTMLTagObj("p", false);
	gaoTags[counter++] = new HTMLTagObj("param", false);
	gaoTags[counter++] = new HTMLTagObj("pre", false);
	gaoTags[counter++] = new HTMLTagObj("progress", false);
	gaoTags[counter++] = new HTMLTagObj("q", false);
	gaoTags[counter++] = new HTMLTagObj("rp", false);
	gaoTags[counter++] = new HTMLTagObj("rt", false);
	gaoTags[counter++] = new HTMLTagObj("ruby", false);
	gaoTags[counter++] = new HTMLTagObj("s", false);			// obsolete
	gaoTags[counter++] = new HTMLTagObj("samp", false);
//	gaoTags[counter++] = new HTMLTagObj("script", false);	// ### removed from input
	gaoTags[counter++] = new HTMLTagObj("section", false);
	gaoTags[counter++] = new HTMLTagObj("select", false);
	gaoTags[counter++] = new HTMLTagObj("small", false);
	gaoTags[counter++] = new HTMLTagObj("source", false);
	gaoTags[counter++] = new HTMLTagObj("span", false);
	gaoTags[counter++] = new HTMLTagObj("strike", false);		// obsolete
	gaoTags[counter++] = new HTMLTagObj("strong", false);
	gaoTags[counter++] = new HTMLTagObj("style", false);
	gaoTags[counter++] = new HTMLTagObj("sub", false);
	gaoTags[counter++] = new HTMLTagObj("sup", false);
	gaoTags[counter++] = new HTMLTagObj("table", false);
	gaoTags[counter++] = new HTMLTagObj("tbody", false);
	gaoTags[counter++] = new HTMLTagObj("td", false);
	gaoTags[counter++] = new HTMLTagObj("textarea", false);
	gaoTags[counter++] = new HTMLTagObj("tfoot", false);
	gaoTags[counter++] = new HTMLTagObj("th", false);
	gaoTags[counter++] = new HTMLTagObj("thead", false);
	gaoTags[counter++] = new HTMLTagObj("time", false);
	gaoTags[counter++] = new HTMLTagObj("title", true);
	gaoTags[counter++] = new HTMLTagObj("tr", false);
	gaoTags[counter++] = new HTMLTagObj("tt", false);
	gaoTags[counter++] = new HTMLTagObj("u", false);			// obsolete
	gaoTags[counter++] = new HTMLTagObj("ul", false);
	gaoTags[counter++] = new HTMLTagObj("var", false);
	gaoTags[counter++] = new HTMLTagObj("video", false);
	gaoTags[counter++] = new HTMLTagObj("wbr", false);			// obsolete
	gaoTags[counter++] = new HTMLTagObj("xmp", false);
}			// initializeTags()


// remove all child nodes
function removeChildNodes(nodeParentID) {
//	try {
		var parentNode = document.getElementById(nodeParentID);

		while (parentNode.firstChild)
			parentNode.removeChild(parentNode.firstChild);
//	}
//	catch (e) {
//		switch(e.name) {
//		}
//	}
}			// removeChildNodes()
