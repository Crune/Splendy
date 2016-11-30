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
    console.log("Component Refreshed!" + comp.length);

    // 필드 코인 설정
    for (var i = 0; i < 6; i++) {
        var cn_num = i+1;
        if (cn_num == 6) {
            cn_num = 0;
        }
        $("#container #side li:nth-child(" + cn_num + ") span").html(getCoin(0, i));
    }

}

function comp_priv(evt) {
    var msg = JSON.parse(evt.body);
    var type = msg.type;
    var cont = msg.cont;

    if (type == 'card') {
        card_prev(cont);
    }
    comp_refresh();
}

var comp = {};
comp.card = {};
comp.cards = {};
comp.coins = {};

function card_prev(data) {
    for (var i = 0, card; card = data[i]; i++) {
        comp.card[card.cd_id] = card;
    }
    isReadPrevComp = true;
}

function card_change(evt) {
    var cards = JSON.parse(evt.body);
    card_changing(cards);
}
function card_changing(data) {
    for (var i=0, card; card = data[i]; i++) {
        comp.cards[card.cd_id] = card;
    }
    comp_refresh();
}

function coin_change(evt) {
    var coins = JSON.parse(evt.body);
    coin_changing(coins);
}
function coin_changing(data) {
    for (var i = 0, coin; coin = data[i]; i++) {
        proCoin(coin.u_id, coin.cn_id, coin.cn_count);
    }
    comp_refresh();
}

function initCoin(u_id, cn_id) {
    if (!comp.coins[u_id]) {
        comp.coins[u_id] = {};
    }
    if (!comp.coins[u_id][cn_id]) {
        comp.coins[u_id][cn_id] = 0;
    }
}
function getCoin(u_id, cn_id) {
    initCoin(u_id, cn_id);
    return comp.coins[u_id][cn_id];
}
function setCoin(u_id, cn_id, amount) {
    initCoin(u_id, cn_id);
    comp.coins[u_id][cn_id] = amount;
}
function proCoin(u_id, cn_id, amount) {
    setCoin(u_id, cn_id, getCoin(u_id, cn_id) + amount);
}