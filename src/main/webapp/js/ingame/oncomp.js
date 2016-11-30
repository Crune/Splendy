var isReadPrevComp = false;

var compPriv;
var cardChange;
var coinChange;

function onComp() {
    compPriv = stompClient.subscribe('/comp/private/'+uid, comp_priv);

    cardChange = stompClient.subscribe('/comp/card/'+rid, card_change);
    coinChange = stompClient.subscribe('/comp/coin/'+rid, coin_change);

    send('comp/join/'+rid, '');
    comp_refresh();
    send('comp/cards', '');
}

function comp_refresh() {
    console.log("Component Refreshed!");

    compMap.get("card");
    compMap.get("coin");
}

function comp_priv(evt) {
    var msg = JSON.parse(evt.body);
    var type = msg.type;
    var cont = msg.cont;

    if (type == 'card') {
        card_prev(data);
    }
    comp_refresh();
}

var compMap = newMap();
compMap.put("card_temp", newMap());
compMap.put("card", newMap());
compMap.put("coin", newMap());

function card_prev(data) {
    len = data.length;
    for (i = 0; i < len; i++) {
        var curCard = data[i];
        compMap.get("card_temp").put(curCard.id, curCard);
    }
    isReadPrevComp = true;
}
function getCInfo(cn_id) {
    return compMap.get("card_temp").get(cn_id);
}

function card_change(evt) {
    var cards = JSON.parse(evt.body);
    card_changing(cards);
}
function card_changing(cards) {
    len = cards.length;
    for (i = 0; i < len; i++) {
        changeCard(cards[i].cn_id, cards[i]);
    }
    comp_refresh();
}
function changeCard(cn_id, reqCard) {
    var org_card = compMap.get("card").get(cn_id);
    if (org_card) {
        compMap.get("card").remove(cn_id);
    }
    compMap.get("card").put(cn_id, reqCard);
}
function getCard(cn_id) {
    return compMap.get("card").get(cn_id);
}

function coin_change(evt) {
    var coins = JSON.parse(evt.body);
    coin_changing(coins);
}
function coin_changing(coins) {
    len = coins.length;
    for (i = 0; i < len; i++) {
        var cur_coin = coins[i];
        proCoin(cur_coin.u_id, cur_coin.cn_id, cur_coin.cn_count);
    }
    comp_refresh();
}

function getCoin(u_id, cn_id) {
    var user = compMap.get("coin").get(u_id);
    return user.get(cn_id);
}
function setCoin(u_id, cn_id, amount) {
    var user = compMap.get("coin").get(u_id);
    if (user == "") {
        compMap.get("coin").put(u_id, newMap());
    }
    var cur = user.get(cn_id);
    if (cur == "") {
        compMap.get("coin").get(u_id).remove(cn_id);
    }
    compMap.get("coin").get(u_id).put(cn_id, amount);
}
function proCoin(u_id, cn_id, amount) {
    setCoin(u_id, cn_id, getCoin(u_id, cn_id) + amount);
}