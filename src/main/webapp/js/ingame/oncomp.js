var isReadPrevComp = false;

var compPriv;
var cardChange;
var coinChange;

function onComp() {
    load_card();

    compPriv = stompClient.subscribe('/comp/private/'+uid, comp_priv);

    cardChange = stompClient.subscribe('/comp/card/'+rid, card_change);
    coinChange = stompClient.subscribe('/comp/coin/'+rid, coin_change);

    comp_refresh();
}

function comp_refresh() {
    console.log("Component Refreshed!" + comp.length);

    // 필드 코인 설정
    for (var i = 1; i <= 6; i++) {
        $("#container #side li#cn_id_"+i+" span").html(getCoin(0, i));
    }

    // 사용자 코인 설정
    for (var i=0, pl; pl=pls.data[pls.order[i]]; i++) {
        for (var j = 1; j < 6; j++) {
            $("#pl"+i+"_cd"+j).html(getCoin(pl.uid, j));
        }
    }

}

function comp_priv(evt) {
    var msg = JSON.parse(evt.body);
    var type = msg.type;
    var cont = msg.cont;

    if (type == 'card') {
    }
    comp_refresh();
}

var comp = {};
comp.card = {};
comp.cards = {};
comp.coins = {};

function card_change(evt) {
    var cards = JSON.parse(evt.body);
    card_changing(cards);
}
function card_changing(data) {
    var toUserList = {}
    var toFieldList = {}
    for (var i=0, card; card = data[i]; i++) {
        if (card.u_id == 0) {
            toFieldList[toUserList.length] = card;
        } else {
            toUserList[toUserList.length] = card;
        }
        comp.cards[card.cd_id]["u_id"] = card.u_id;
        comp.cards[card.cd_id]["n_hold"] = card.n_hold;
    }
    card_animate(toUserList, toFieldList);
}
function card_animate(to_user, to_field) {

    for (var i=0, card; card = to_user[i]; i++) {
        $("#card_" + card.cd_id).animate({
            left: '250px',
            opacity: '0.5',
            height: '150px',
            width: '150px'
        });
    }
}

function setCard(u_id, cd_id, n_hold) {
    comp.cards[cd_id]["u_id"] = u_id;
    comp.cards[cd_id]["n_hold"] = n_hold;
    comp.cards[cd_id]["card"] = comp.card[cd_id];
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

