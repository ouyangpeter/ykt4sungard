-- ���²������̻�Ӷ��ģ�������ֵ�

insert into YKT_CUR.T_PIF_SYSKEY (KEY_CODE, KEY_NAME, KEY_VALUE, MAX_VALUE, KEY_TYPE)
values ('TIF_SUBSIDY_NO', '�������κ�', 1, 65535, '0');

INSERT INTO YKT_CUR.T_PIF_CUR_SYSPARA (ID, VALUE, PARA_NAME, USETIME, OPTIME, UNIT, COMMENTS, IS_DISPLAY)
values (16, '400.00', '���ʲ����������', '20001212121212', '20001212121212', '', null, '1');


insert into YKT_CUR.T_PIF_TRADECODE (TRADECODE, TRADEMSG, SPLITCFG, FLAG)
values (847231, 'Ӷ�𻮲�', null, null);
insert into YKT_CUR.T_PIF_TRADECODE (TRADECODE, TRADEMSG, SPLITCFG, FLAG)
values (847232, 'Ӷ���ۿ�', null, null);

insert into YKT_CUR.T_TIF_SUBJECT (SUBNO, SUBNAME, SUBTYPE, DIRECT, KIND, FATHER, "LEVEL", ENDFLAG, SPARE1, SPARE2, SPARE3)
values ('505', '�̻�Ӷ��', 5, 2, 0, '530', 2, 1, '550', null, null);

insert into YKT_CUR.T_AIF_ACCOUNT (ACCOUNT_ID, ACT_TYPE, CUSTOMER_ID, CUT_NAME, CUT_TYPE, STUEMP_NO, CARD_ID, PURSE_ID, CARD_TYPE, SUBNO, PASSWORD, CURRENT_STATE, ISAUTOTRA, LAST_BALA, LAST_FREEBALA, LAST_FROZEBALA, CUR_BALA, CUR_FREEBALA, CUR_FROZEBALA, OUT_BALA, IN_BALA, OUT_COUNT, IN_COUNT, CARD_BALANCE, NOBALA_NUM, CONSUME_COUNT, OPEN_DATE, OPEN_TIME, CLOSE_DATE, CLOSE_TIME, RESERVE_1, RESERVE_2, RESERVE_3, COMMENTS, DEPOSIT_BALA)
values ('5051            ', 7, 0, '�̻�Ӷ��', 0, null, 0, 0, 0, '505', null, 1, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null);

insert into YKT_CUR.T_AIF_ACCOUNT (ACCOUNT_ID, ACT_TYPE, CUSTOMER_ID, CUT_NAME, CUT_TYPE, STUEMP_NO, CARD_ID, PURSE_ID, CARD_TYPE, SUBNO, PASSWORD, CURRENT_STATE, ISAUTOTRA, LAST_BALA, LAST_FREEBALA, LAST_FROZEBALA, CUR_BALA, CUR_FREEBALA, CUR_FROZEBALA, OUT_BALA, IN_BALA, OUT_COUNT, IN_COUNT, CARD_BALANCE, NOBALA_NUM, CONSUME_COUNT, OPEN_DATE, OPEN_TIME, CLOSE_DATE, CLOSE_TIME, RESERVE_1, RESERVE_2, RESERVE_3, COMMENTS, DEPOSIT_BALA)
values ('5061            ', 7, 0, '�����ۿ�', 0, null, 0, 0, 0, '506', null, 1, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null);

insert into YKT_CUR.T_TIF_SUBJECT (SUBNO, SUBNAME, SUBTYPE, DIRECT, KIND, FATHER, "LEVEL", ENDFLAG, SPARE1, SPARE2, SPARE3)
values ('506', '�����ۿ�', 5, 2, 0, '530', 2, 1, '560', null, null);

insert into YKT_CUR.T_PIF_DICTIONARY (DICT_NO, DICT_VALUE, DICT_NAME, DICT_CAPTION)
values (19, '4', '�ʻ�״̬', '�����˻�');



INSERT INTO YKT_CUR.T_PIF_DICTIONARY ( DICT_NO, DICT_VALUE, DICT_NAME, DICT_CAPTION ) 
VALUES ( 91, '0', 'Ӷ������', '�̶�����' );
INSERT INTO YKT_CUR.T_PIF_DICTIONARY ( DICT_NO, DICT_VALUE, DICT_NAME, DICT_CAPTION ) 
VALUES ( 91, '1', 'Ӷ������', '��������' ) ;


INSERT INTO YKT_CUR.T_TIF_CFGSPLIT (TXTYPE, TXNAME, DBFLAG, CRFLAG, DBSUBNO, CRSUBNO, DBACTNO, CRACTNO, FUNDTYPE, SUMMARY) 
VALUES (59, 'Ӷ�𻮲�', 1, 2, '202', '505', NULL, '5051', NULL, NULL); 
INSERT INTO YKT_CUR.T_TIF_CFGSPLIT (TXTYPE, TXNAME, DBFLAG, CRFLAG, DBSUBNO, CRSUBNO, DBACTNO, CRACTNO, FUNDTYPE, SUMMARY)
VALUES (60, 'Ӷ���ۿ�', 2, 1, '505', '202', '5051', NULL, NULL, NULL);