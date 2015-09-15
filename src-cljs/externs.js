var anychart = {};
anychart.theme = {defaultTooltip: {title: {enabled: false}}};
anychart.data = {};

anychart.data.Set = function() {};
anychart.data.Set.prototype.data = function() {};

/** @return {anychart.data.Set} */
anychart.data.set = function() {};

anychart.SimpleChart = function() {};
anychart.SimpleChart.prototype.container = function() {};
anychart.SimpleChart.prototype.title = function() {};
anychart.SimpleChart.prototype.draw = function() {};

/** @return {anychart.SimpleChart} */
anychart.bar = function() {};

/** @return {anychart.SimpleChart} */
anychart.column = function() {};

/** @return {anychart.SimpleChart} */
anychart.pie = function() {};

/** @return {anychart.SimpleChart} */
anychart.line = function() {};
