//javascript only code to call the program
var info = {
	"items" : {
		"c001" : {
			"parent" : "c000",
			"title" : "Phones"
		},
		"c002" : {
			"parent" : "c000",
			"title" : "Laptops"
		},
		"c003" : {
			"parent" : "c000",
			"title" : "Accessories"
		},
		"p001" : {
			"parent" : "c001",
			"manufacturer" : "Apple",
			"title" : "iPhone 10",
			"price" : 499,
			"releaseDate" : "2018-04-23T18:25:43.511Z"
		},
		"p002" : {
			"parent" : "c002",
			"manufacturer" : "Apple",
			"title" : "Macbook Pro 2017",
			"price" : 1999,
			"releaseDate" : "2017-04-23T18:25:43.511Z"
		},
		"p003" : {
			"parent" : "c002",
			"manufacturer" : "Dell",
			"title" : "XPS 13",
			"price" : 1399,
			"releaseDate" : "2017-04-23T18:25:43.511Z"
		},
		"p004" : {
			"parent" : "c001",
			"manufacturer" : "Samsung",
			"title" : "Samsung Galaxy S10",
			"price" : 399,
			"releaseDate" : "2017-04-23T18:25:43.511Z"
		},
		"p005" : {
			"parent" : "c003",
			"manufacturer" : "Apple",
			"title" : "Apple Charger 12W",
			"price" : 99,
			"releaseDate" : "2016-12-23T18:25:43.511Z"
		},
		"p006" : {
			"parent" : "c003",
			"manufacturer" : "Samsung",
			"title" : "Samsung Charger 12W",
			"price" : 89,
			"releaseDate" : "2017-04-23T18:25:43.511Z"
		}
	}

};

var Merchandise = {
	Constants : {
		LOCALE : "en-us",
		OBJECT_TYPE : {
			CATEGORY : "category",
			PRODUCT : "product"
		},
		PRICE_SYMBOL_CODE : "&#36;" // set price symbol as constant to US dollar
									// for now until response has this
	},
	parseContent : function(merchItems) {

		if (Object.keys(merchItems.items).length === 0
				&& merchItems.items.constructor === Object) {

			var itemMain = document.createElement('div');
			itemMain.className = 'product productContent';
			itemMain.innerHTML = 'No product to display'
			document.getElementById('content').appendChild(itemMain);
		} else {
			for ( var key in merchItems.items) {
				var item = merchItems.items[key];
				var itemMain = document.createElement('div');
				itemMain.id = key;
				var type = this.Util.getObjectType(key);

				if (document.getElementById(item.parent) == null) {
					var newParent = document.createElement('div');
					newParent.id = item.parent;
					document.getElementById('content').appendChild(newParent);
				}

				if (type == Merchandise.Constants.OBJECT_TYPE.CATEGORY) {
					itemMain.className += ' '
							+ Merchandise.Constants.OBJECT_TYPE.CATEGORY;
					// category title
					if (typeof item.title !== 'undefined') {
						itemTitle = document.createElement('div');
						itemTitle.className = 'categoryTitle';
						itemTitle.innerHTML = item.title;
						itemMain.appendChild(itemTitle);
					}
				} else if (type == Merchandise.Constants.OBJECT_TYPE.PRODUCT) {
					itemMain.className += ' '
							+ Merchandise.Constants.OBJECT_TYPE.PRODUCT;
					// product title
					if (typeof item.title !== 'undefined') {
						var itemTitle = document.createElement('div');
						itemTitle.className = 'productTitle';
						itemTitle.innerHTML = item.title;
						itemMain.appendChild(itemTitle);
					}

					// Description
					if (typeof item.manufacturer !== 'undefined'
							|| typeof item.releaseDate !== 'undefined') {
						var itemContent = document.createElement('div');
						itemContent.className = 'productContent';
						itemDescription = '';
						if (typeof item.manufacturer !== 'undefined') {
							itemDescription += "Manufactured by "
									+ item.manufacturer;
						}
						if (typeof item.releaseDate !== 'undefined'
								&& this.Util.isValidDate(item.releaseDate)) {
							if (typeof item.manufacturer !== 'undefined') {
								itemDescription += ", ";
							}
							itemDescription += "to be released in "
									+ this.Util.formatDate(item.releaseDate);
						}
						itemContent.innerHTML = itemDescription;
						itemMain.appendChild(itemContent);
					}

					// price
					if (typeof item.price !== 'undefined') {
						var itemPrice = document.createElement('div');
						itemPrice.className = 'productPrice';
						itemPrice.innerHTML = Merchandise.Constants.PRICE_SYMBOL_CODE
								+ item.price;
						itemMain.appendChild(itemPrice);

					}
				}
				document.getElementById(item.parent).appendChild(itemMain);
			}
		}
	},
	Util : {
		/**
		 * format ISO date
		 */
		formatDate : function(isoDate) {
			var date = null;
			if (this.isValidDate(isoDate)) {
				date = new Date(isoDate).toLocaleString(
						Merchandise.Constants.LOCALE, {
							month : "long"
						})
						+ " "
						+ new Date(isoDate).getDate()
						+ ", "
						+ new Date(isoDate).getFullYear();
			}
			return date;
		},
		/**
		 * TODO: use a more robust library if available for date checker
		 */
		isValidDate : function(isoDate) {
			var valid = false;
			if (isoDate && typeof (Date.parse(isoDate)) === 'number'
					&& !isNaN(Date.parse(isoDate))) {
				valid = true
			} else {
				Merchandise.Util.log
						.error('Merchandise.Util.formatDate: invalid date object '
								+ isoDate);
			}
			return valid;
		},
		/**
		 * check if an object is a category or a product return: String
		 */
		getObjectType : function(key) {
			var type = null;
			if (key && typeof key === "string") {
				if (key.charAt(0).toLowerCase() == 'c') {
					type = Merchandise.Constants.OBJECT_TYPE.CATEGORY;
				} else if (key.charAt(0).toLowerCase() == 'p') {
					type = Merchandise.Constants.OBJECT_TYPE.PRODUCT;
				}
			} else {
				Merchandise.Util.log
						.error('Merchandise.Util.getObjectType: invalid parameter');
			}
			return type;
		},
		/**
		 * TODO: don't use console for prod log
		 */
		log : {
			/**
			 * log all catchable error
			 */
			error : function(errorMessage) {
				console.error(errorMessage);
			},
			/**
			 * log for activity tracking
			 */
			info : function(message) {
				console.log(message);
			}
		}
	}
}

// main method
function loadDoc() {
	var xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			Merchandise.parseContent(JSON.parse(this.responseText));
		}
	};
	// change domain and port on local settings
	// contextPath value found at defaultLayout.jsp
	if(contextPath) {
		xhttp.open("GET", contextPath+"/viewList", true);
	} else {
		xhttp.open("GET", "http://localhost:8080/shop/viewList", true); 
	}
	xhttp.send();
}
loadDoc();
// Merchandise.parseContent(info);
