/* 사용자 */
CREATE TABLE MY_SCHEMA.USER_INFO (
	U_ID INTEGER NOT NULL, /* 사용자키 */
	U_NICK VARCHAR2(50), /* 닉네임 */
	U_EMAIL VARCHAR2(40) NOT NULL, /* E-Mail */
	U_PW VARCHAR2(255) NOT NULL, /* 비밀번호 */
	U_ENABLED INTEGER DEFAULT 1, /* 사용가능 */
	U_N_LOCKED INTEGER DEFAULT 1, /* !잠김 */
	U_N_EXPIRED INTEGER DEFAULT 1, /* !만료 */
	U_N_CREDENT INTEGER DEFAULT 1, /* !연동만료 */
	U_REG DATE DEFAULT sysdate NOT NULL /* 등록일 */
);

COMMENT ON TABLE MY_SCHEMA.USER_INFO IS '사용자';

COMMENT ON COLUMN MY_SCHEMA.USER_INFO.U_ID IS '사용자키';

COMMENT ON COLUMN MY_SCHEMA.USER_INFO.U_NICK IS '닉네임';

COMMENT ON COLUMN MY_SCHEMA.USER_INFO.U_EMAIL IS 'E-Mail';

COMMENT ON COLUMN MY_SCHEMA.USER_INFO.U_PW IS '비밀번호';

COMMENT ON COLUMN MY_SCHEMA.USER_INFO.U_ENABLED IS '사용가능';

COMMENT ON COLUMN MY_SCHEMA.USER_INFO.U_N_LOCKED IS '!잠김';

COMMENT ON COLUMN MY_SCHEMA.USER_INFO.U_N_EXPIRED IS '!만료';

COMMENT ON COLUMN MY_SCHEMA.USER_INFO.U_N_CREDENT IS '!연동만료';

COMMENT ON COLUMN MY_SCHEMA.USER_INFO.U_REG IS '등록일';

CREATE UNIQUE INDEX MY_SCHEMA.PK_USER_INFO
	ON MY_SCHEMA.USER_INFO (
		U_ID ASC
	);

ALTER TABLE MY_SCHEMA.USER_INFO
	ADD
		CONSTRAINT PK_USER_INFO
		PRIMARY KEY (
			U_ID
		);

/* 게임방 */
CREATE TABLE MY_SCHEMA.ROOM (
	RM_ID INTEGER NOT NULL, /* 게임목록키 */
	RM_TITLE VARCHAR2(255), /* 제목 */
	RM_PW VARCHAR2(255), /* 비밀번호 */
	RM_INFO CLOB, /* 설명 */
	RM_PL_LIMITS INTEGER, /* 인원제한 */
	RM_STATE INTEGER /* 상태 */
);

COMMENT ON TABLE MY_SCHEMA.ROOM IS '게임방';

COMMENT ON COLUMN MY_SCHEMA.ROOM.RM_ID IS '게임목록키';

COMMENT ON COLUMN MY_SCHEMA.ROOM.RM_TITLE IS '제목';

COMMENT ON COLUMN MY_SCHEMA.ROOM.RM_PW IS '비밀번호';

COMMENT ON COLUMN MY_SCHEMA.ROOM.RM_INFO IS '설명';

COMMENT ON COLUMN MY_SCHEMA.ROOM.RM_PL_LIMITS IS '인원제한';

COMMENT ON COLUMN MY_SCHEMA.ROOM.RM_STATE IS '상태';

CREATE UNIQUE INDEX MY_SCHEMA.PK_ROOM
	ON MY_SCHEMA.ROOM (
		RM_ID ASC
	);

ALTER TABLE MY_SCHEMA.ROOM
	ADD
		CONSTRAINT PK_ROOM
		PRIMARY KEY (
			RM_ID
		);

/* 참가자 */
CREATE TABLE MY_SCHEMA.PLAYER (
	U_ID INTEGER NOT NULL, /* 사용자키 */
	RM_ID INTEGER NOT NULL, /* 게임목록키 */
	PL_STATE INTEGER, /* 상태 */
	PL_REG DATE DEFAULT sysdate /* 참가일 */
);

COMMENT ON TABLE MY_SCHEMA.PLAYER IS '참가번호는 참가일 순';

COMMENT ON COLUMN MY_SCHEMA.PLAYER.U_ID IS '사용자키';

COMMENT ON COLUMN MY_SCHEMA.PLAYER.RM_ID IS '게임목록키';

COMMENT ON COLUMN MY_SCHEMA.PLAYER.PL_STATE IS '상태';

COMMENT ON COLUMN MY_SCHEMA.PLAYER.PL_REG IS '참가일';

CREATE UNIQUE INDEX MY_SCHEMA.PK_PLAYER
	ON MY_SCHEMA.PLAYER (
		U_ID ASC,
		RM_ID ASC
	);

ALTER TABLE MY_SCHEMA.PLAYER
	ADD
		CONSTRAINT PK_PLAYER
		PRIMARY KEY (
			U_ID,
			RM_ID
		);

/* 카드 */
CREATE TABLE MY_SCHEMA.CARD (
	CD_ID INTEGER NOT NULL, /* 카드키 */
	CD_TYPE VARCHAR2(255), /* 분류 */
	CD_NAME VARCHAR2(255), /* 이름 */
	CD_IMG VARCHAR2(255), /* 이미지 */
	CD_INFO CLOB /* 설명 */
);

COMMENT ON TABLE MY_SCHEMA.CARD IS '카드';

COMMENT ON COLUMN MY_SCHEMA.CARD.CD_ID IS '카드키';

COMMENT ON COLUMN MY_SCHEMA.CARD.CD_TYPE IS '분류';

COMMENT ON COLUMN MY_SCHEMA.CARD.CD_NAME IS '이름';

COMMENT ON COLUMN MY_SCHEMA.CARD.CD_IMG IS '이미지';

COMMENT ON COLUMN MY_SCHEMA.CARD.CD_INFO IS '설명';

CREATE UNIQUE INDEX MY_SCHEMA.PK_CARD
	ON MY_SCHEMA.CARD (
		CD_ID ASC
	);

ALTER TABLE MY_SCHEMA.CARD
	ADD
		CONSTRAINT PK_CARD
		PRIMARY KEY (
			CD_ID
		);

/* 덱 */
CREATE TABLE MY_SCHEMA.DECK (
	DK_ID INTEGER NOT NULL, /* 덱키 */
	DK_TYPE VARCHAR2(255), /* 분류 */
	DK_NAME VARCHAR2(255), /* 덱이름 */
	DK_INFO CLOB /* 덱설명 */
);

COMMENT ON TABLE MY_SCHEMA.DECK IS '덱';

COMMENT ON COLUMN MY_SCHEMA.DECK.DK_ID IS '덱키';

COMMENT ON COLUMN MY_SCHEMA.DECK.DK_TYPE IS '분류';

COMMENT ON COLUMN MY_SCHEMA.DECK.DK_NAME IS '덱이름';

COMMENT ON COLUMN MY_SCHEMA.DECK.DK_INFO IS '덱설명';

CREATE UNIQUE INDEX MY_SCHEMA.PK_DECK
	ON MY_SCHEMA.DECK (
		DK_ID ASC
	);

ALTER TABLE MY_SCHEMA.DECK
	ADD
		CONSTRAINT PK_DECK
		PRIMARY KEY (
			DK_ID
		);

/* 코인 */
CREATE TABLE MY_SCHEMA.COIN (
	CN_ID INTEGER NOT NULL, /* 코인키 */
	CN_TYPE VARCHAR2(255), /* 분류 */
	CN_NAME VARCHAR2(255), /* 이름 */
	CN_IMG VARCHAR2(255), /* 이미지 */
	CN_INFO CLOB /* 설명 */
);

COMMENT ON TABLE MY_SCHEMA.COIN IS '코인';

COMMENT ON COLUMN MY_SCHEMA.COIN.CN_ID IS '코인키';

COMMENT ON COLUMN MY_SCHEMA.COIN.CN_TYPE IS '분류';

COMMENT ON COLUMN MY_SCHEMA.COIN.CN_NAME IS '이름';

COMMENT ON COLUMN MY_SCHEMA.COIN.CN_IMG IS '이미지';

COMMENT ON COLUMN MY_SCHEMA.COIN.CN_INFO IS '설명';

CREATE UNIQUE INDEX MY_SCHEMA.PK_COIN
	ON MY_SCHEMA.COIN (
		CN_ID ASC
	);

ALTER TABLE MY_SCHEMA.COIN
	ADD
		CONSTRAINT PK_COIN
		PRIMARY KEY (
			CN_ID
		);

/* 메시지 */
CREATE TABLE MY_SCHEMA.MSG (
	M_ID INTEGER NOT NULL, /* 메시지키 */
	RM_ID INTEGER, /* 게임목록키 */
	M_TYPE INTEGER, /* 분류 */
	M_AUTHOR VARCHAR2(255), /* 작성 */
	M_CONT CLOB, /* 내용 */
	M_REG DATE DEFAULT sysdate /* 작성일시 */
);

COMMENT ON TABLE MY_SCHEMA.MSG IS '작성자 NULL은 시스템';

COMMENT ON COLUMN MY_SCHEMA.MSG.M_ID IS '메시지키';

COMMENT ON COLUMN MY_SCHEMA.MSG.RM_ID IS '게임목록키';

COMMENT ON COLUMN MY_SCHEMA.MSG.M_TYPE IS '분류';

COMMENT ON COLUMN MY_SCHEMA.MSG.M_AUTHOR IS '작성';

COMMENT ON COLUMN MY_SCHEMA.MSG.M_CONT IS '내용';

COMMENT ON COLUMN MY_SCHEMA.MSG.M_REG IS '작성일시';

CREATE UNIQUE INDEX MY_SCHEMA.PK_MSG
	ON MY_SCHEMA.MSG (
		M_ID ASC
	);

ALTER TABLE MY_SCHEMA.MSG
	ADD
		CONSTRAINT PK_MSG
		PRIMARY KEY (
			M_ID
		);

/* 컴포넌트 이동 이력 */
CREATE TABLE MY_SCHEMA.MOVE (
	MV_ID INTEGER NOT NULL, /* 이동이력키 */
	RM_ID INTEGER NOT NULL, /* 게임목록키 */
	MV_FROM INTEGER, /* 시작점 */
	MV_TO INTEGER, /* 종료점 */
	CD_ID INTEGER, /* 카드키 */
	CN_ID INTEGER, /* 코인키 */
	MV_NUM INTEGER, /* 이동수량 */
	MV_REG DATE DEFAULT sysdate /* 이동일시 */
);

COMMENT ON TABLE MY_SCHEMA.MOVE IS '컴포넌트 이동 이력';

COMMENT ON COLUMN MY_SCHEMA.MOVE.MV_ID IS '이동이력키';

COMMENT ON COLUMN MY_SCHEMA.MOVE.RM_ID IS '게임목록키';

COMMENT ON COLUMN MY_SCHEMA.MOVE.MV_FROM IS '시작점';

COMMENT ON COLUMN MY_SCHEMA.MOVE.MV_TO IS '종료점';

COMMENT ON COLUMN MY_SCHEMA.MOVE.CD_ID IS '카드키';

COMMENT ON COLUMN MY_SCHEMA.MOVE.CN_ID IS '코인키';

COMMENT ON COLUMN MY_SCHEMA.MOVE.MV_NUM IS '이동수량';

COMMENT ON COLUMN MY_SCHEMA.MOVE.MV_REG IS '이동일시';

CREATE UNIQUE INDEX MY_SCHEMA.PK_MOVE
	ON MY_SCHEMA.MOVE (
		MV_ID ASC
	);

ALTER TABLE MY_SCHEMA.MOVE
	ADD
		CONSTRAINT PK_MOVE
		PRIMARY KEY (
			MV_ID
		);

/* 카드목록 */
CREATE TABLE MY_SCHEMA.CARDS (
	DK_ID INTEGER NOT NULL, /* 덱키 */
	CD_ID INTEGER NOT NULL /* 카드키 */
);

COMMENT ON TABLE MY_SCHEMA.CARDS IS '카드목록';

COMMENT ON COLUMN MY_SCHEMA.CARDS.DK_ID IS '덱키';

COMMENT ON COLUMN MY_SCHEMA.CARDS.CD_ID IS '카드키';

CREATE UNIQUE INDEX MY_SCHEMA.PK_CARDS
	ON MY_SCHEMA.CARDS (
		DK_ID ASC,
		CD_ID ASC
	);

ALTER TABLE MY_SCHEMA.CARDS
	ADD
		CONSTRAINT PK_CARDS
		PRIMARY KEY (
			DK_ID,
			CD_ID
		);

ALTER TABLE MY_SCHEMA.PLAYER
	ADD
		CONSTRAINT FK_USER_INFO_TO_PLAYER
		FOREIGN KEY (
			U_ID
		)
		REFERENCES MY_SCHEMA.USER_INFO (
			U_ID
		);

ALTER TABLE MY_SCHEMA.PLAYER
	ADD
		CONSTRAINT FK_ROOM_TO_PLAYER
		FOREIGN KEY (
			RM_ID
		)
		REFERENCES MY_SCHEMA.ROOM (
			RM_ID
		);

ALTER TABLE MY_SCHEMA.MSG
	ADD
		CONSTRAINT FK_ROOM_TO_MSG
		FOREIGN KEY (
			RM_ID
		)
		REFERENCES MY_SCHEMA.ROOM (
			RM_ID
		);

ALTER TABLE MY_SCHEMA.MOVE
	ADD
		CONSTRAINT FK_ROOM_TO_MOVE
		FOREIGN KEY (
			RM_ID
		)
		REFERENCES MY_SCHEMA.ROOM (
			RM_ID
		);

ALTER TABLE MY_SCHEMA.MOVE
	ADD
		CONSTRAINT FK_CARD_TO_MOVE
		FOREIGN KEY (
			CD_ID
		)
		REFERENCES MY_SCHEMA.CARD (
			CD_ID
		);

ALTER TABLE MY_SCHEMA.MOVE
	ADD
		CONSTRAINT FK_COIN_TO_MOVE
		FOREIGN KEY (
			CN_ID
		)
		REFERENCES MY_SCHEMA.COIN (
			CN_ID
		);

ALTER TABLE MY_SCHEMA.CARDS
	ADD
		CONSTRAINT FK_CARD_TO_CARDS
		FOREIGN KEY (
			CD_ID
		)
		REFERENCES MY_SCHEMA.CARD (
			CD_ID
		);

ALTER TABLE MY_SCHEMA.CARDS
	ADD
		CONSTRAINT FK_DECK_TO_CARDS
		FOREIGN KEY (
			DK_ID
		)
		REFERENCES MY_SCHEMA.DECK (
			DK_ID
		);