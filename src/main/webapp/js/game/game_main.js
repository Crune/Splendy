var chatSock = null;
var player1 = new Player("dohyun", 1);
var player2 = new Player("yoon", 2);
var player3 = new Player("minjung", 3);
var player4 = new Player("jinkyu", 4);

var cardManager = new CardManager();
var INIT_POINT = 0;  //소모되는 행동 포인트
var ONE_POINT = 1;
var TWO_POINT = 2;
var THREE_POINT = 3;

var orderCount = 0;
var players = [ player1, player2, player3, player4 ];

var jewelWhiteValue;	//플레이어가 보석을 얻어올 때마다 현재 남은 보석 갯수로 초기화 되는 변수
var jewelGreenValue;
var jewelBlackValue;
var jewelRedValue;
var jewelBlueValue;
var jewelGoldValue;

var heroCardList = new Array();
var lev3CardList = new Array();
var lev2CardList = new Array();
var lev1CardList = new Array();

var rest_cardValue = {
		rest_heroCardValue : 5,
		rest_lev3CardValue : 15,
		rest_lev2CardValue : 25,
		rest_lev1CardValue : 35
}

var getTurnPlayer = function (){
	for(var i in players){
		if(players[i].getTurn() === true) {
			return players[i];
		}			
	}
	return;
}

var getNextPlayer = function(){
	for(var i in players){
		if(players[i].getTurn() === true){
			i = parseInt(i);
			if(i === 3){
				return players[0];
			}
			return players[i+1];
		}
	}
	return;
}

$(document).ready(
		function() {			
			// 페이지가 시작됨과 동시에 소켓 서버 주소로 접속한다.
			chatSock = new SockJS("http://" + window.location.host + "/ws");
			
			//필드의 보석 초기화
			field_jewel = new Field_jewel();
			field_jewel.initJewel();
			
			
			chatSock.onopen = function() {				
				player1.toggleTurn();
				$("#player1").css("background-color", "red");
				
				wssend('cardRequest', 'init_levN');
				wssend('cardRequest', 'init_lev3');
				wssend('cardRequest', 'init_lev2');
				wssend('cardRequest', 'init_lev1');
				
				$("#player1_name").html(player1.getUserId());
				$("#player2_name").html(player2.getUserId());
				$("#player3_name").html(player3.getUserId());
				$("#player4_name").html(player4.getUserId());
			};
					
			chatSock.onmessage = function(evt) {				
				var card = JSON.parse(evt.data);				
				if(card.type === "init_levN"){
					var initHeroCards = cardManager.setInitLevN(card.cont);
					for(var i in initHeroCards){
						i = parseInt(i);
						var template = Handlebars.compile($(".heroCard_details").html());												
						$("#heroCard_detail_" + (i+1)).html(template(initHeroCards[i]));
						heroCardList.push(initHeroCards[i]);						
					}	
					console.log(heroCardList);
				}
				
				if(card.type === "init_lev3"){
					var lev3Cards = cardManager.setInitLev3(card.cont);
					for(var i in lev3Cards){						
						i = parseInt(i);
						var template = Handlebars.compile($(".heroCard_details").html());												
						$("#lev3Card_detail_" + (i+1)).html(template(lev3Cards[i]));
						lev3CardList.push(lev3Cards[i]);
					}	
					
					console.log(lev3CardList);
				}
				
				if(card.type === "init_lev2"){
					var lev2Cards = cardManager.setInitLev2(card.cont);
					for(var i in lev2Cards){						
						i = parseInt(i);
						var template = Handlebars.compile($(".heroCard_details").html());												
						$("#lev2Card_detail_" + (i+1)).html(template(lev2Cards[i]));
						lev2CardList.push(lev2Cards[i]);
					}					
					console.log(lev2CardList);
				}
				
				if(card.type === "init_lev1"){
					var lev1Cards = cardManager.setInitLev1(card.cont);
					for(var i in lev1Cards){						
						i = parseInt(i);
						var template = Handlebars.compile($(".heroCard_details").html());												
						$("#lev1Card_detail_" + (i+1)).html(template(lev1Cards[i]));
						lev1CardList.push(lev1Cards[i]);
					}						
					console.log(lev1CardList);
				}
				
				if(card.type === "getHeroCard"){
					console.log(evt);
				}
				
				if(card.type === "cardCountPro"){					
					var json = JSON.parse(evt.data);
					rest_cardValue.rest_heroCardValue = json.cont.rest_heroCardValue;
					rest_cardValue.rest_lev3CardValue = json.cont.rest_lev3CardValue;
					rest_cardValue.rest_lev2CardValue = json.cont.rest_lev2CardValue;
					rest_cardValue.rest_lev1CardValue = json.cont.rest_lev1CardValue;					
				}
			};

			chatSock.onclose = function() {
				alert('연결 종료');
			};

			
			$("#jewel_white_img").click(
					function() {						
						var player = getTurnPlayer();
						var nextPlayer = getNextPlayer();
	
						if( jewelWhiteValue === 0){
							alert('사용가능한 보석이 없습니다');
							return;
						}	
						
						if (player.getActionPoint() === THREE_POINT) {
							player.setAllJewelStatusFalse();
							player.toggleTurn();
							player.setActionPoint(INIT_POINT);
							nextPlayer.toggleTurn();
							$("#player" + (player.getOrder())).css("background-color", "tomato");
							$("#player" + (nextPlayer.getOrder())).css("background-color", "red");							
							return;
						}
						if (player.getWhiteJewelStatus() === false) {							
							jewelWhiteValue = player.getWhite();
							player.setActionPoint(ONE_POINT);
							player.toggleWhiteJewelStatus(); // true
						} else if (player.getWhiteJewelStatus() === true) {
							if(player.getActionPoint() === TWO_POINT){
								alert('다른 보석을 고르세요.');
								return;
							}
							jewelWhiteValue = player.getWhite();
							player.setActionPoint(TWO_POINT);
						}
																								
						for(var i in players){
							players[i].getJewelsValue();  //버튼을 누를 때 마다 각 플레이어 객체의 보석 값을 가져온다.
							console.log(players[i]);
						}
					});

			$("#jewel_green_img").click(
					function() {
						var player = getTurnPlayer();
						var nextPlayer = getNextPlayer();
						if( jewelGreenValue === 0){
							alert('사용가능한 보석이 없습니다');
							return;
						}	
						
						if (player.getActionPoint() === THREE_POINT) {
							player.setAllJewelStatusFalse();
							player.toggleTurn();
							player.setActionPoint(INIT_POINT);
							nextPlayer.toggleTurn();
							$("#player" + (player.getOrder())).css("background-color", "tomato");
							$("#player" + (nextPlayer.getOrder())).css("background-color", "red");	
							return;
						}

						if (player.getGreenJewelStatus() === false) {
							jewelGreenValue = player.getGreen();
							player.setActionPoint(ONE_POINT);
							player.toggleGreenJewelStatus(); // true
						} else if (player.getGreenJewelStatus() === true) {
							if(player.getActionPoint() === TWO_POINT){
								alert('다른 보석을 고르세요.');
								return;
							}
							jewelGreenValue = player.getGreen();
							player.setActionPoint(TWO_POINT);
						}
						
						for(var i in players){
							players[i].getJewelsValue();  //버튼을 누를 때 마다 각 플레이어 객체의 보석 값을 가져온다.
							console.log(players[i]);
						}
					});
			$("#jewel_blue_img").click(
					function() {
						var player = getTurnPlayer();
						var nextPlayer = getNextPlayer();
					
						if( jewelBlueValue === 0){
							alert('사용가능한 보석이 없습니다');
							return;
						}	
												
						if (player.getActionPoint() === THREE_POINT) {
							player.setAllJewelStatusFalse();
							player.toggleTurn();
							player.setActionPoint(INIT_POINT);
							nextPlayer.toggleTurn();
							$("#player" + (player.getOrder())).css("background-color", "tomato");
							$("#player" + (nextPlayer.getOrder())).css("background-color", "red");	
							return;
						}
						
						if (player.getBlueJewelStatus() === false) {
							jewelBlueValue = player.getBlue();
							player.setActionPoint(ONE_POINT);
							player.toggleBlueJewelStatus(); // true
						} else if (player.getBlueJewelStatus() === true) {
							if(player.getActionPoint() === TWO_POINT){
								alert('다른 보석을 고르세요.');
								return;
							}
							jewelBlueValue = player.getBlue();
							player.setActionPoint(TWO_POINT);
						}
					
						for(var i in players){
							players[i].getJewelsValue();  //버튼을 누를 때 마다 각 플레이어 객체의 보석 값을 가져온다.
							console.log(players[i]);
						}
					});
			$("#jewel_red_img").click(
					function() {
						var player = getTurnPlayer();
						var nextPlayer = getNextPlayer();
						
						if( jewelRedValue === 0){
							alert('사용가능한 보석이 없습니다');
							return;
						}	
						
						if (player.getActionPoint() === THREE_POINT) {
							player.setAllJewelStatusFalse();
							player.toggleTurn();
							player.setActionPoint(INIT_POINT);
							nextPlayer.toggleTurn();
							$("#player" + (player.getOrder())).css("background-color", "tomato");
							$("#player" + (nextPlayer.getOrder())).css("background-color", "red");	
							return;
						}

						if (player.getRedJewelStatus() === false) {
							jewelRedValue = player.getRed();
							player.setActionPoint(ONE_POINT);
							player.toggleRedJewelStatus(); // true
						} else if (player.getRedJewelStatus() === true) {
							if(player.getActionPoint() === TWO_POINT){
								alert('다른 보석을 고르세요.');
								return;
							}
							jewelRedValue = player.getRed();
							player.setActionPoint(TWO_POINT);
						}
						
						for(var i in players){
							players[i].getJewelsValue();  //버튼을 누를 때 마다 각 플레이어 객체의 보석 값을 가져온다.
							console.log(players[i]);
						}
					});
			$("#jewel_black_img").click(
					function() {
						var player = getTurnPlayer();
						var nextPlayer = getNextPlayer();
						
						if (player.getActionPoint() === THREE_POINT) {
							player.setAllJewelStatusFalse();
							player.toggleTurn();
							player.setActionPoint(INIT_POINT);
							nextPlayer.toggleTurn();
							$("#player" + (player.getOrder())).css("background-color", "tomato");
							$("#player" + (nextPlayer.getOrder())).css("background-color", "red");	
							return;
						}
						
						if (player.getBlackJewelStatus() === false) {
							jewelBlackValue = player.getBlack();
							player.setActionPoint(ONE_POINT);
							player.toggleBlackJewelStatus(); // true
						} else if (player.getBlackJewelStatus() === true) {
							if(player.getActionPoint() === TWO_POINT){
								alert('다른 보석을 고르세요.');
								return;
							}
							jewelBlackValue = player.getBlack();
							player.setActionPoint(TWO_POINT);
						}
						
						for(var i in players){
							players[i].getJewelsValue();  //버튼을 누를 때 마다 각 플레이어 객체의 보석 값을 가져온다.
							console.log(players[i]);
						}
					});

			$(".jewel_img").click(function(){
				field_jewel.modalJewel();
				$('#myModal').modal('show');
			});
			
			$("#heroCard_detail_1").click(function (){
				console.log(heroCardList[0]);
				if(rest_cardValue.rest_heroCardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_heroCardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			$("#heroCard_detail_2").click(function (){
				console.log(heroCardList[1]);
				if(rest_cardValue.rest_heroCardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_heroCardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			$("#heroCard_detail_3").click(function (){
				console.log(heroCardList[2]);
				if(rest_cardValue.rest_heroCardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_heroCardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			$("#heroCard_detail_4").click(function (){
				console.log(heroCardList[3]);
				if(rest_cardValue.rest_heroCardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_heroCardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			$("#heroCard_detail_5").click(function (){
				console.log(heroCardList[4]);
				if(rest_cardValue.rest_heroCardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_heroCardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev3Card_detail_1").click(function (){
				card
				if(rest_cardValue.rest_lev3CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev3CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev3Card_detail_2").click(function (){
				console.log(lev3CardList[1]);
				if(rest_cardValue.rest_lev3CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev3CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			$("#lev3Card_detail_3").click(function (){
				console.log(lev3CardList[2]);
				if(rest_cardValue.rest_lev3CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev3CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			$("#lev3Card_detail_4").click(function (){
				console.log(lev3CardList[3]);
				if(rest_cardValue.rest_lev3CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev3CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
						
			$("#lev3Card_detail_5").click(function (){
				console.log(lev3CardList[4]);
				if(rest_cardValue.rest_lev3CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev3CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev2Card_detail_1").click(function (){
				console.log(lev2CardList[0]);
				if(rest_cardValue.rest_lev2CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev2CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev2Card_detail_2").click(function (){
				console.log(lev2CardList[1]);
				if(rest_cardValue.rest_lev2CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev2CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev2Card_detail_3").click(function (){
				console.log(lev2CardList[2]);
				if(rest_cardValue.rest_lev2CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev2CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev2Card_detail_4").click(function (){
				if(rest_cardValue.rest_lev2CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev2CardValue--;
				console.log(lev2CardList[3]);
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev2Card_detail_5").click(function (){
				console.log(lev2CardList[4]);
				if(rest_cardValue.rest_lev2CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev2CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev1Card_detail_1").click(function (){
				var player = getTurnPlayer();
				var card = lev1CardList[0];
				console.log(cardManager.checkCardSpec(player, card));
				if(rest_cardValue.rest_lev1CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				
				rest_cardValue.rest_lev1CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev1Card_detail_2").click(function (){
				console.log(lev1CardList[1]);
				if(rest_cardValue.rest_lev1CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev1CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev1Card_detail_3").click(function (){
				console.log(lev1CardList[2]);
				if(rest_cardValue.rest_lev1CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev1CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev1Card_detail_4").click(function (){
				console.log(lev1CardList[3]);
				if(rest_cardValue.rest_lev1CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev1CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
			$("#lev1Card_detail_5").click(function (){
				console.log(lev1CardList[4]);
				if(rest_cardValue.rest_lev1CardValue == 0){
					alert('더이상 카드가 없습니다.');
					return;
				}
				rest_cardValue.rest_lev1CardValue--;
				var json = JSON.stringify(rest_cardValue);				
				wssend('cardCount', json);
				wssend('cardRequest', 'getHeroCard');
			});
			
		});
