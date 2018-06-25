******************** STL ver 2.3 eye design crosstalk model - 2subspace ********************

*** Notice *** -------------------------------------------------*
* STL基本形ののアイパターン整形用のテンプレートです．

*--------------------------------------------------------------------*


*** STL Circuit *** -------------------------------------------------*

Vs1             101     0       LFSR( 0 2 100p 100p 100p 2g 3 [8,7,4,3,2])
Rin1            101     102     50

*** Subspaces ***
X1_STL_3        102     0       optpt1  0       ckt_Z50 Length=100m N=1
C1              optpt1  0       10p
X2_STL_8        optpt1  0       optpt2  0       ckt_Z50 Length=100m N=1
C2              optpt2  0       10p
X3_STL_3        optpt2  0       optpt3  0       ckt_Z50 Length=100m N=1
*****************

RT1             optpt3  0       50

*--------------------------------------------------------------------*


*** Versus Circuit *** ----------------------------------------------*

*** Versus TL 1 ***
Vs1001          1001    0       LFSR( 0 2 100p 100p 100p 2g 3 [8,7,4,3,2])
Rinvs1         1001    1002    50
Tvs1           1002    0       vspt1   0       z0=50   TD=630.8p
Tvs2           vspt1   0       vspt2   0       z0=50   TD=630.8p
Tvs3           vspt2   0       vspt3   0       z0=50   TD=630.8p
RTvs1          vspt3   0       50

*--------------------------------------------------------------------*


*** Trigger Circuit *** ---------------------------------------------*
Vs10001         10001   0       PULSE( 0 2 0p 100p 100p 0.4n 1n )
Rin10001        10001   10002   50
T10001          10002   0       trgpt1   0       z0=50   TD=630.8p
T10002          trgpt1  0       trgpt2   0       z0=50   TD=630.8p
T10003          trgpt2  0       trgpt3   0       z0=50   TD=630.8p
RT10001         trgpt3  0       50

*--------------------------------------------------------------------*


*** Netlist Commands *** --------------------------------------------*

* プリントアウト幅（80 or 132）
.WIDTH OUT=132
.PRINT v(1001) v(optpt1) v(optpt2) v(optpt3) v(vspt1) v(vspt2) v(vspt3)  v(trgpt1) v(trgpt2) v(trgpt3)
* 合計 29801 プロット（25GHz サンプリング, 50サイクル, 1周期250プロット）
.TRAN 4p 50n 0p

.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z40_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z45_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z50_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z55_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z60_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z65_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z70_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z75_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z80_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z85_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z90_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z95_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z100_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z105_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z110_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z115_sta.hsp
.INCLUDE src/test/resources/template/char_imp/single_MSline_1GHz/Z120_sta.hsp
.END

*--------------------------------------------------------------------*


