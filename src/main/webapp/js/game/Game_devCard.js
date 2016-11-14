function DevCard(){
	this.type;
	this.point;
	
	this.black;
	this.green;
	this.white;
	this.red;
	this.blue;
	this.gold;
}

DevCard.prototype = {
		getType : function() {
			return this.type;
		},
		
		getPoint : function() {
			return this.point;
		}
		
}
