CREATE TABLE android_metadata (locale TEXT);
INSERT INTO android_metadata VALUES('en_US');
CREATE TABLE crypt (_id INTEGER PRIMARY KEY, Name , Type , Clan , Adv , _Group , Capacity , Disciplines , CardText , _Set , Title , Banned , Artist , Abombwe , Animalism , Auspex , Celerity , Chimerstry , Daimoinon , Dementation , Dominate , Fortitude , Melpominee , Mytherceria , Necromancy , Obeah , Obfuscate , Obtenebration , Potence , Presence , Protean , Quietus , Sanguinus , Serpentis , Spiritus , Temporis , Thanatosis , Thaumaturgy , Valeren , Vicissitude , Visceratika );
CREATE TABLE library (_id INTEGER PRIMARY KEY, Name , Type , Clan , Discipline , PoolCost , BloodCost , ConvictionCost , BurnOption , CardText , FlavorText , _Set , Requirement , Banned , Artist );
CREATE TABLE favorite_cards (CardType NUMERIC, CardId NUMERIC);
CREATE INDEX IX_FAVORITE_CARDS_CARDID on favorite_cards(CardId);
CREATE TABLE decks (_id integer primary key, Name);
CREATE TABLE deck_cards (_id integer primary key, DeckId integer, CardType integer, CardId integer, CardNum integer);

.mode csv
.headers on

.import vtescrypt.csv crypt_import
.import vteslib.csv library_import

insert into crypt (Name , Type , Clan , Adv , _Group , Capacity , Disciplines , CardText , _Set , Title , Banned , Artist , Abombwe , Animalism , Auspex , Celerity , Chimerstry , Daimoinon , Dementation , Dominate , Fortitude , Melpominee , Mytherceria , Necromancy , Obeah , Obfuscate , Obtenebration , Potence , Presence , Protean , Quietus , Sanguinus , Serpentis , Spiritus , Temporis , Thanatosis , Thaumaturgy , Valeren , Vicissitude , Visceratika) select * from crypt_import;

insert into library (Name , Type , Clan , Discipline , PoolCost , BloodCost , ConvictionCost , BurnOption , CardText , FlavorText , _Set , Requirement , Banned , Artist) select * from library_import;


drop table crypt_import;
drop table library_import;

