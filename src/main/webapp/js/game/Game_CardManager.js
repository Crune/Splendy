function CardManager(){
	this.initCards;
	this.initLev1;
	this.initLev2;
	this.initLev3;
	this.initLevN;
	this.playerSpec;
	this.cardSpec;
	
} 

CardManager.prototype = {
		setInitLevN : function(data){
			var initHeroCardList = new Array();
			
			for(var i in data){				
				heroCard = new HeroCard();
				heroCard.setId(data[i].id);
				heroCard.setCode(data[i].code);
				heroCard.setImg(data[i].img);
				heroCard.setInfo(data[i].info);
				heroCard.setName(data[i].name);
				heroCard.setType(data[i].type);
				heroCard.setPoint(data[i].point);
				heroCard.setBlack(data[i].black);
				heroCard.setBlue(data[i].blue);
				heroCard.setGreen(data[i].green);
				heroCard.setRed(data[i].red);
				heroCard.setWhite(data[i].white);
				
				initHeroCardList.push(heroCard);				
			}
			return initHeroCardList;			
		},
		
		setInitLev1 : function(data){
			var initLev1CardList = new Array();
			
			for(var i in data){				
				lev1Card = new Lev1Card();
				lev1Card.setId(data[i].id);
				lev1Card.setCode(data[i].code);
				lev1Card.setImg(data[i].img);
				lev1Card.setInfo(data[i].info);
				lev1Card.setName(data[i].name);
				lev1Card.setType(data[i].type);
				lev1Card.setPoint(data[i].point);
				lev1Card.setBlack(data[i].black);
				lev1Card.setBlue(data[i].blue);
				lev1Card.setGreen(data[i].green);
				lev1Card.setRed(data[i].red);
				lev1Card.setWhite(data[i].white);
				
				initLev1CardList.push(lev1Card);
			}
			return initLev1CardList;			
		},
		
		setInitLev2 : function(data){
			var initLev2CardList = new Array();
			
			for(var i in data){				
				lev2Card = new Lev2Card();
				lev2Card.setId(data[i].id);
				lev2Card.setCode(data[i].code);
				lev2Card.setImg(data[i].img);
				lev2Card.setInfo(data[i].info);
				lev2Card.setName(data[i].name);
				lev2Card.setType(data[i].type);
				lev2Card.setPoint(data[i].point);
				lev2Card.setBlack(data[i].black);
				lev2Card.setBlue(data[i].blue);
				lev2Card.setGreen(data[i].green);
				lev2Card.setRed(data[i].red);
				lev2Card.setWhite(data[i].white);
				
				initLev2CardList.push(lev2Card);
			}
			return initLev2CardList;			
		},
		
		setInitLev3 : function(data){
			var initLev3CardList = new Array();
			
			for(var i in data){				
				lev3Card = new Lev3Card();
				lev3Card.setId(data[i].id);
				lev3Card.setCode(data[i].code);
				lev3Card.setImg(data[i].img);
				lev3Card.setInfo(data[i].info);
				lev3Card.setName(data[i].name);
				lev3Card.setType(data[i].type);
				lev3Card.setPoint(data[i].point);
				lev3Card.setBlack(data[i].black);
				lev3Card.setBlue(data[i].blue);
				lev3Card.setGreen(data[i].green);
				lev3Card.setRed(data[i].red);
				lev3Card.setWhite(data[i].white);
				
				initLev3CardList.push(lev3Card);
			}
			return initLev3CardList;			
		},
		
		checkCardSpec : function(player, card){
			console.log(player.getWhiteJewel_value());
			if(player.getWhiteJewel_value() == card.white){
				return true;
			}
			return false;
		}
		
}

