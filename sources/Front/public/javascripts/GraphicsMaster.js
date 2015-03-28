var GraphicsMaster = Base.extend({
	options: {
		xType: "inputSize",
		yType: "compressionRate"
	},
    constructor: function(options, proxy, view) {
    	$.extend(this.options, options);
    	this.proxy = proxy == null ? new GraphicsMaster.Proxy() : proxy;
    	this.view = view == null ? new GraphicsMaster.View(this.options) : view;
    	
    	this.xType = this.options.xType;
    	this.yType = this.options.yType;
    	this.model = null;
    	this.deactivated = [];
    	
        this.__attachEvents();
    },
    __attachEvents: function() {
    	var _this = this;
    	this.view.onBuild(function() {
			_this.proxy.buildPlot(_this.xType, _this.yType);
		});
    	this.view.onParamsChange(function(xType, yType) {
			_this.xType = xType;
			_this.yType = yType;
		});
    	this.view.onPlotChange(function(item) {
    		var idx = _this.deactivated.indexOf(item);
    		if (idx == -1)
    			_this.deactivated.push(item);
    		else
    			_this.deactivated.splice(idx, 1);
    		_this.__buildPlot();
    	});
		
    	this.proxy.onBuildSuccess(this.__buildPlot, this);
    	this.proxy.onBuildFails(function(message) {
			alert(message);
		});
    },
    __buildPlot: function (model) {
    	if (model != undefined)
    		this.model = model;
    		
    	if (this.model.curves.length == 0) {
    	   this.view.clear();
    	   alert("The plot is empty for the specified parameters.");
    	   return;
    	}
    	
    	var points = [];
        var series = [];

        for (var i = 0; i < this.model.curves.length; i++) {
            var curve = this.model.curves[i];
        	if (this.deactivated.indexOf(curve.caption) == -1)
        	   points.push(curve.points.length > 0 ? curve.points : [[]]);
        	else
        		points.push([[]]);
            series.push({label:curve.caption, color:curve.color });
        }
        this.view.clear();
    	this.view.renderPlot(points, series, this.model.xLabel, this.model.yLabel, this.deactivated);
    }
}, {
	Proxy: Base.extend({
		constructor: function() {
			this.onBuildSuccess = Event.create();
			this.onBuildFails = Event.create();
		},
		buildPlot: function(xType, yType) {
			var _this = this;
			$.ajax({
	            type: 'get',
	            url: '/statistics/build',
	            dataType: 'json',
	            data: 'xType=' + xType + '&yType=' + yType,
	            success: function (data) {
	            	_this.onBuildSuccess(data);
	            },
	            error: function (e) {
	            	_this.onBuildFails("An error appears during the plot building. Try to reload the page and rebuild the plot.");
	            }
	        });
		}
	}),
	View: Base.extend({
		constructor: function(options) {
			this.onBuild = Event.create();
			this.onParamsChange = Event.create();
			this.onPlotChange = Event.create();
			
			this.currentClass = 'master_axis_factors_item__current';
			
			this.__bindEvents();
			this.__init(options);
		},
		__bindEvents: function() {
			var _this = this;
			$('#masterButton').on('click', function(){
				_this.onBuild();
	        });
			
			$('.master').on('click', '.master_axis_factors_item', function(){
	            var __this = $(this);

	            if (__this.hasClass(_this.currentClass)) return;

	            var thisCurrent = __this.siblings('.' + _this.currentClass);
	            var thisCurrentFactor = thisCurrent.attr('data-factor');
	            var thisNewFactor = __this.attr('data-factor');
	            var siblingCurrent = __this.parents('.master_axis').siblings().find('.' + _this.currentClass);
	            var siblingCurrentFactor = siblingCurrent.attr('data-factor');

	            thisCurrent.removeClass(_this.currentClass);
	            __this.addClass(_this.currentClass);
	            if (thisNewFactor == siblingCurrentFactor){
	                siblingCurrent.removeClass(_this.currentClass).siblings('[data-factor=' + thisCurrentFactor + ']').addClass(_this.currentClass);
	                siblingCurrentFactor = thisCurrentFactor;
	            }

	            if ($(this).parents('.master_axis').id == 'MasterAxisX')
	            	_this.onParamsChange(thisNewFactor, siblingCurrentFactor);
	            else
	            	_this.onParamsChange(siblingCurrentFactor, thisNewFactor);
	        });
		},
		__init: function(options) {
			$('#MasterAxisX li[data-factor="'+ options.xType +'"]').addClass(this.currentClass);
			$('#MasterAxisY li[data-factor="'+ options.yType +'"]').addClass(this.currentClass);
		},
		clear: function() {
		  $('#PlotArea').empty();
		},
		renderPlot: function(points, series, xLabel, yLabel, deactivated) {
			$.jqplot('PlotArea', points, {
                "series":series,
                "legend":{
                    "show":true, "location":"ne", "showSwatch":true, "xoffset":12, "yoffset":12 },
                "axes":{
                    "xaxis":{
                        "label":xLabel
                    },
                    "yaxis":{
                        "label":yLabel
                    }
                }
            });
            this.__bindAlgorithmesChoice();
            for (var i = 0; i < deactivated.length; i++)
                this.__disableColor(deactivated[i]);
		},
		__bindAlgorithmesChoice: function() {
	    	var i = 0;
	        $('.jqplot-table-legend-swatch').parents('tr').each(function(){
	        	$(this).css('position', 'relative').css('z-index', '9999');
	        	var colorBox = $('.jqplot-table-legend-swatch', this);
	        	colorBox.attr('data-defaultColor', colorBox[0].style.backgroundColor);
	        	var caption = $(this).children().last().text();
	        	colorBox.attr('data-caption', caption);
	        });

	        var _this = this;
	        $('.jqplot-table-legend-swatch').parents('tr').on('click', function(){
	        	var colorBox = $('.jqplot-table-legend-swatch', this);
	            if(colorBox.hasClass('jqplot-table-legend-swatch__disabled')){
	                var color = colorBox.attr('data-defaultColor');
	                colorBox.css('border-color', color);
	            } 
	            else
	            	colorBox.css('border-color', '#ccc');
	            colorBox.toggleClass('jqplot-table-legend-swatch__disabled');
	            
	            _this.onPlotChange($('td', this).last().text());
	        });
	    },
	    __disableColor: function(caption) {
	    	var colorBox = $('.jqplot-table-legend-swatch[data-caption="'+ caption +'"]');
	    	colorBox.css('border-color', '#ccc');
	    	colorBox.toggleClass('jqplot-table-legend-swatch__disabled');
	    }
	})
});
