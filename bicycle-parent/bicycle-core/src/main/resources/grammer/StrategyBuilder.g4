grammar StrategyBuilder;
@header {
	package grammer;
}

strategyBuilder: statement+;

statement: namedIteratorStatement | actionStatement;

actionStatement: longEntryActionStatement | longExitActionStatement | shortEntryActionStatement | shortExitActionStatement;
longEntryActionStatement: 'buy' ruleBuilder;
longExitActionStatement: 'sell' ruleBuilder;
shortEntryActionStatement: 'short' ruleBuilder;
shortExitActionStatement: 'cover' ruleBuilder;

iterator: 'optimize(' NUMBER ',' NUMBER ',' NUMBER ',' NUMBER ')';
namedIteratorStatement: WORD '=' iterator;
parameter: NUMBER | WORD | iterator;

ruleBuilder: dayOfWeekRuleBuilder | weekOfMonthRuleBuilder | monthRuleBuilder | stopGainRuleBuilder
	| stopLossRuleBuilder | waitForBarCountRuleBuilder | indicatorRuleBuilder | ruleBuilder LOGICAL_OPERATOR ruleBuilder
	| negatedRuleBuilder;
	
dayOfWeekRuleBuilder: 'dayOfWeek(' DAY (',' DAY)* ')';
weekOfMonthRuleBuilder: 'weekOfMonth(' INTEGER (',' INTEGER)* ')';
monthRuleBuilder: 'month(' MONTH (',' MONTH)* ')';
stopGainRuleBuilder:  'stopGain(' parameter ')';
stopLossRuleBuilder: 'stopLoss(' parameter ')';
waitForBarCountRuleBuilder: 'after' parameter 'bars';
indicatorRuleBuilder: indicatorBuilder COMPARATOR indicatorBuilder;
negatedRuleBuilder: NEGATION ruleBuilder;

indicatorBuilder: openBuilder | highBuilder | lowBuilder | closeBuilder | volumeBuilder | trueRangeBuilder 
	| typicalPriceBuilder | closeLocationBuilder | constantBuilder | chopBuilder | cciBuilder | atrBuilder 
	| lossBuilder | gainBuilder | smaBuilder | emaBuilder | mmaBuilder | rsiBuilder | fallingStrengthBuilder
	| risingStrengthBuilder | highestValueBuilder | lowestValueBuilder | previousValueBuilder
	| varianceBuilder | meanDevBuilder | stdDevBuilder | indicatorBuilder ARITHMATIC_OPERATOR indicatorBuilder;

openBuilder: 'open';
highBuilder: 'high';
lowBuilder: 'low';
closeBuilder: 'close';
volumeBuilder: 'volume';
trueRangeBuilder: 'trueRange';
typicalPriceBuilder: 'typicalPrice';
closeLocationBuilder: 'closeLocation';
constantBuilder: 'constant(' parameter ')';
chopBuilder: 'chop(' parameter ')';
cciBuilder: 'cci(' parameter ')';
atrBuilder: 'atr(' parameter ')';
lossBuilder: 'loss(' parameter ')';
gainBuilder: 'gain(' parameter ')';
smaBuilder: 'sma(' indicatorBuilder ',' parameter ')';
emaBuilder: 'ema(' indicatorBuilder ',' parameter ')';
mmaBuilder: 'mma(' indicatorBuilder ',' parameter ')';
rsiBuilder: 'rsi(' indicatorBuilder ',' parameter ')';
fallingStrengthBuilder: 'fallingStrength(' indicatorBuilder ',' parameter ')';
risingStrengthBuilder: 'risingStrength(' indicatorBuilder ',' parameter ')';
highestValueBuilder: 'highest(' indicatorBuilder ',' parameter ')';
lowestValueBuilder: 'lowest(' indicatorBuilder ',' parameter ')';
previousValueBuilder: 'previous(' indicatorBuilder ',' parameter ')';
varianceBuilder: 'variance(' indicatorBuilder ',' parameter ')';
meanDevBuilder: 'meanDev(' indicatorBuilder ',' parameter ')';
stdDevBuilder: 'stdDev(' indicatorBuilder ',' parameter ')';

COMPARATOR: '=' | '>' | '<';
NEGATION: 'not';
LOGICAL_OPERATOR : 'and' | 'or' ;
ARITHMATIC_OPERATOR : '+' | '-' | '*' | '/' | '^';
WS : [ \t\n]+ -> skip;
NUMBER: INTEGER | FLOAT;
INTEGER: DIGIT+;
WORD : [a-zA-Z0-9_]+;
DAY: 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY' | 'SUNDAY';
MONTH: 'JANUARY' | 'FEBRUARY' | 'MARCH' | 'APRIL' | 'MAY' | 'JUNE' | 'JULY' | 'AUGUST' | 'SEPTEMBER' | 'OCTOBER' | 'NOVEMBER' | 'DECEMBER';
FLOAT: DIGIT+ '.' DIGIT+;
fragment DIGIT : [0-9] ;