
attach 'Vampidroid-Unaligned.db' as unaligned;

delete from crypt;

insert into crypt (Name,
    Type,
    Clan,
    Adv,
    _Group,
    Capacity,
    Disciplines,
    CardText,
    _Set,
    Title,
    Banned,
    Artist,
    Abombwe,
    Animalism,
    Auspex,
    Celerity,
    Chimerstry,
    Daimoinon,
    Dementation,
    Dominate,
    Fortitude,
    Melpominee,
    Mytherceria,
    Necromancy,
    Obeah,
    Obfuscate,
    Obtenebration,
    Potence,
    Presence,
    Protean,
    Quietus,
    Sanguinus,
    Serpentis,
    Spiritus,
    Temporis,
    Thanatosis,
    Thaumaturgy,
    Valeren,
    Vicissitude,
    Visceratika)
select Name,
    Type,
    Clan,
    Adv,
    _Group,
    Capacity,
    Disciplines,
    CardText,
    _Set,
    Title,
    Banned,
    Artist,
    Abombwe,
    Animalism,
    Auspex,
    Celerity,
    Chimerstry,
    Daimoinon,
    Dementation,
    Dominate,
    Fortitude,
    Melpominee,
    Mytherceria,
    Necromancy,
    Obeah,
    Obfuscate,
    Obtenebration,
    Potence,
    Presence,
    Protean,
    Quietus,
    Sanguinus,
    Serpentis,
    Spiritus,
    Temporis,
    Thanatosis,
    Thaumaturgy,
    Valeren,
    Vicissitude,
    Visceratika from unaligned.crypt where _Set not like '%TU:C%' and _Set not like '%DM:C%' and _Set not like '%Promo-2015%';


update crypt set _id = 6399 where _id = 63;
update crypt set _id = 63 where _id = 62;
update crypt set _id = 62 where _id = 6399;

update crypt set _id = 7999 where _id = 79;
update crypt set _id = 79 where _id = 78;
update crypt set _id = 78 where _id = 7999;

update crypt set _id = 10299 where _id = 102;
update crypt set _id = 102 where _id = 101;
update crypt set _id = 101 where _id = 10299;

update crypt set _id = 16999 where _id = 169;
update crypt set _id = 169 where _id = 168;
update crypt set _id = 168 where _id = 16999;

update crypt set _id = 31899 where _id = 318;
update crypt set _id = 318 where _id = 316;
update crypt set _id = 316 where _id = 31899;

update crypt set _id = 35099 where _id = 350;
update crypt set _id = 350 where _id = 349;
update crypt set _id = 349 where _id = 35099;

update crypt set _id = 45499 where _id = 454;
update crypt set _id = 454 where _id = 453;
update crypt set _id = 453 where _id = 45499;

update crypt set _id = 52599 where _id = 525;
update crypt set _id = 525 where _id = 523;
update crypt set _id = 523 where _id = 52599;

update crypt set _id = 54099 where _id = 540;
update crypt set _id = 540 where _id = 539;
update crypt set _id = 539 where _id = 54099;


update crypt set _id = 58399 where _id = 583;
update crypt set _id = 58499 where _id = 584;
update crypt set _id = 58699 where _id = 586;
update crypt set _id = 58799 where _id = 587;
update crypt set _id = 58899 where _id = 588;

update crypt set _id = 583 where _id = 58499;
update crypt set _id = 584 where _id = 58699;
update crypt set _id = 586 where _id = 58799;
update crypt set _id = 587 where _id = 58899;
update crypt set _id = 588 where _id = 58399;


update crypt set _id = 64799 where _id = 647;
update crypt set _id = 647 where _id = 646;
update crypt set _id = 646 where _id = 64799;

update crypt set _id = 68699 where _id = 686;
update crypt set _id = 686 where _id = 685;
update crypt set _id = 685 where _id = 68699;

update crypt set _id = 74399 where _id = 743;
update crypt set _id = 743 where _id = 741;
update crypt set _id = 741 where _id = 74399;

update crypt set _id = 76199 where _id = 761;
update crypt set _id = 761 where _id = 760;
update crypt set _id = 760 where _id = 76199;

update crypt set _id = 76999 where _id = 769;
update crypt set _id = 769 where _id = 768;
update crypt set _id = 768 where _id = 76999;

update crypt set _id = 78899 where _id = 788;
update crypt set _id = 788 where _id = 787;
update crypt set _id = 787 where _id = 78899;

update crypt set _id = 85899 where _id = 858;
update crypt set _id = 858 where _id = 857;
update crypt set _id = 857 where _id = 85899;

update crypt set _id = 86699 where _id = 866;
update crypt set _id = 866 where _id = 865;
update crypt set _id = 865 where _id = 86699;

update crypt set _id = 90199 where _id = 901;
update crypt set _id = 901 where _id = 900;
update crypt set _id = 900 where _id = 90199;

update crypt set _id = 101499 where _id = 1014;
update crypt set _id = 1014 where _id = 1013;
update crypt set _id = 1013 where _id = 101499;

update crypt set _id = 103099 where _id = 1030;
update crypt set _id = 1030 where _id = 1028;
update crypt set _id = 1028 where _id = 103099;

update crypt set _id = 127199 where _id = 1271;
update crypt set _id = 1271 where _id = 1270;
update crypt set _id = 1270 where _id = 127199;

update crypt set _id = 131399 where _id = 1313;
update crypt set _id = 1313 where _id = 1312;
update crypt set _id = 1312 where _id = 131399;


insert into crypt (Name,
    Type,
    Clan,
    Adv,
    _Group,
    Capacity,
    Disciplines,
    CardText,
    _Set,
    Title,
    Banned,
    Artist,
    Abombwe,
    Animalism,
    Auspex,
    Celerity,
    Chimerstry,
    Daimoinon,
    Dementation,
    Dominate,
    Fortitude,
    Melpominee,
    Mytherceria,
    Necromancy,
    Obeah,
    Obfuscate,
    Obtenebration,
    Potence,
    Presence,
    Protean,
    Quietus,
    Sanguinus,
    Serpentis,
    Spiritus,
    Temporis,
    Thanatosis,
    Thaumaturgy,
    Valeren,
    Vicissitude,
    Visceratika)
select Name,
    Type,
    Clan,
    Adv,
    _Group,
    Capacity,
    Disciplines,
    CardText,
    _Set,
    Title,
    Banned,
    Artist,
    Abombwe,
    Animalism,
    Auspex,
    Celerity,
    Chimerstry,
    Daimoinon,
    Dementation,
    Dominate,
    Fortitude,
    Melpominee,
    Mytherceria,
    Necromancy,
    Obeah,
    Obfuscate,
    Obtenebration,
    Potence,
    Presence,
    Protean,
    Quietus,
    Sanguinus,
    Serpentis,
    Spiritus,
    Temporis,
    Thanatosis,
    Thaumaturgy,
    Valeren,
    Vicissitude,
    Visceratika from unaligned.crypt where (_Set like '%TU:C%' or _Set like '%DM:C%' or _Set like '%Promo-2015%');
    
    
    
    
    
delete from library;

insert into library (Name,
    Type,
    Clan,
    Discipline,
    PoolCost,
    BloodCost,
    ConvictionCost,
    BurnOption,
    CardText,
    FlavorText,
    _Set,
    Requirement,
    Banned,
    Artist)
   
select
Name,
    Type,
    Clan,
    Discipline,
    PoolCost,
    BloodCost,
    ConvictionCost,
    BurnOption,
    CardText,
    FlavorText,
    _Set,
    Requirement,
    Banned,
    Artist
from unaligned.library where _Set not like '%TU:C%' and _Set not like '%DM:C%' and _Set not like '%Promo-2015%';


update library set _id = 8199 where _id = 81;
update library set _id = 81 where _id = 80;
update library set _id = 80 where _id = 8199;

update library set _id = 21199 where _id = 211;
update library set _id = 211 where _id = 210;
update library set _id = 210 where _id = 21199;

update library set _id = 22499 where _id = 224;
update library set _id = 224 where _id = 223;
update library set _id = 223 where _id = 22499;

update library set _id = 30899 where _id = 308;
update library set _id = 308 where _id = 307;
update library set _id = 307 where _id = 30899;

update library set _id = 31299 where _id = 312;
update library set _id = 312 where _id = 311;
update library set _id = 311 where _id = 31299;

update library set _id = 36699 where _id = 366;
update library set _id = 36799 where _id = 367;
update library set _id = 36899 where _id = 368;
update library set _id = 36999 where _id = 369;
update library set _id = 37099 where _id = 370;

update library set _id = 366 where _id = 36799;
update library set _id = 367 where _id = 36899;
update library set _id = 368 where _id = 36999;
update library set _id = 369 where _id = 37099;
update library set _id = 370 where _id = 36699;

update library set _id = 39299 where _id = 392;
update library set _id = 392 where _id = 391;
update library set _id = 391 where _id = 39299;

update library set _id = 41799 where _id = 417;
update library set _id = 417 where _id = 416;
update library set _id = 416 where _id = 41799;

update library set _id = 55699 where _id = 556;
update library set _id = 556 where _id = 555;
update library set _id = 555 where _id = 55699;

update library set _id = 72199 where _id = 721;
update library set _id = 721 where _id = 720;
update library set _id = 720 where _id = 72199;

update library set _id = 75299 where _id = 752;
update library set _id = 752 where _id = 751;
update library set _id = 751 where _id = 75299;

update library set _id = 79599 where _id = 795;
update library set _id = 795 where _id = 794;
update library set _id = 794 where _id = 79599;

update library set _id = 88799 where _id = 887;
update library set _id = 88899 where _id = 888;
update library set _id = 88999 where _id = 889;

update library set _id = 887 where _id = 88899;
update library set _id = 888 where _id = 88999;
update library set _id = 889 where _id = 88799;


update library set _id = 95599 where _id = 955;
update library set _id = 955 where _id = 954;
update library set _id = 954 where _id = 95599;

update library set _id = 119199 where _id = 1191;
update library set _id = 1191 where _id = 1190;
update library set _id = 1190 where _id = 119199;

update library set _id = 124799 where _id = 1247;
update library set _id = 1247 where _id = 1246;
update library set _id = 1246 where _id = 124799;

update library set _id = 150999 where _id = 1509;
update library set _id = 1509 where _id = 1508;
update library set _id = 1508 where _id = 150999;

update library set _id = 163599 where _id = 1635;
update library set _id = 163699 where _id = 1636;
update library set _id = 163799 where _id = 1637;

update library set _id = 1635 where _id = 163699;
update library set _id = 1636 where _id = 163799;
update library set _id = 1637 where _id = 163599;

update library set _id = 166499 where _id = 1664;
update library set _id = 1664 where _id = 1663;
update library set _id = 1663 where _id = 166499;

update library set _id = 169699 where _id = 1696;
update library set _id = 169799 where _id = 1697;
update library set _id = 169899 where _id = 1698;

update library set _id = 1696 where _id = 169799;
update library set _id = 1697 where _id = 169899;
update library set _id = 1698 where _id = 169699;

update library set _id = 182699 where _id = 1826;
update library set _id = 1826 where _id = 1825;
update library set _id = 1825 where _id = 182699;

update library set _id = 188799 where _id = 1887;
update library set _id = 1887 where _id = 1886;
update library set _id = 1886 where _id = 188799;

update library set _id = 204399 where _id = 2043;
update library set _id = 2043 where _id = 2042;
update library set _id = 2042 where _id = 204399;


insert into library (Name,
    Type,
    Clan,
    Discipline,
    PoolCost,
    BloodCost,
    ConvictionCost,
    BurnOption,
    CardText,
    FlavorText,
    _Set,
    Requirement,
    Banned,
    Artist)
   
select
Name,
    Type,
    Clan,
    Discipline,
    PoolCost,
    BloodCost,
    ConvictionCost,
    BurnOption,
    CardText,
    FlavorText,
    _Set,
    Requirement,
    Banned,
    Artist
from unaligned.library where _Set like '%TU:C%' or _Set like '%DM:C%' or _Set like '%Promo-2015%';


