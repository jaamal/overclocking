var FeedbackControl = Base.extend({
	constructor: function(view, options) {
		this.options = options;
		this.view = view != null ? view : new FeedbackControl.View();
		
		var _this = this;
		this.view.onSend(function(from, message, respondTo) {
			_this.sendFeedback(from, message, respondTo);
		});
	},
	sendFeedback: function(from, message, respondTo) {
		//TODO: unable to receive success on ajax POST, but server correctly added the data 
		var _this = this;
		$.ajax({
            type: 'post',
            url: this.options.sendUrl,
            dataType: 'json',
            data: JSON.stringify({from: from, message: message, respondTo: respondTo}),
            complete: function() {
            	_this.view.close();
            	_this.view.clear();
            }
        });
	}
}, {
	View: Base.extend({
		constructor: function() {
		    this.showFeedbackPopup = $('.showFeedbackPopup');
		    this.feedbackPopup = $('.feedbackPopup');
		    this.feedbackPopupWrap = $('.feedbackPopupWrap');
		    this.wantRespondCheckbox = $('#wantRespond');
		    this.wantRespondField = $('#wantRespondField');
		    
		    this.feedbackFrom = $('#feedbackFrom');
		    this.feedbackMessage = $('#feedbackMessage');
		    this.feederEmail = $('#feederEmail');
		    this.sendBtn = $('#feedbackSendBtn');
			
			this.onSend = Event.create();
			
			this.__attachEvents();
		    if (this.wantRespondCheckbox.is(':checked')) this.wantRespondField.show();
		},
		clear: function() {
			this.feedbackFrom.empty();
			this.feedbackMessage.empty();
			this.feederEmail.empty();
		},
		close: function() {
			this.__toggleFeedbackPopup();
		},
		__attachEvents: function(){
			var _this = this;
			this.sendBtn.on('click', function() {
				_this.onSend(_this.feedbackFrom.val(), _this.feedbackMessage.val(), _this.feederEmail.val());
			});
			
		    this.showFeedbackPopup.on('click', function(e){
		        e.stopPropagation();
		        _this.__toggleFeedbackPopup();
		        return false;
		    });
		    this.feedbackPopup.on('click', function(e){
		        e.stopPropagation();
		    });
		    $('body').on('click', function(){
		        if (_this.feedbackPopup.hasClass('feedbackPopup__opened'))
		        	_this.__toggleFeedbackPopup();
		    });
		    this.wantRespondCheckbox.on('change', function(){
		        _this.wantRespondField.toggle();
		        _this.feederEmail.focus();
		    });
		},
		__toggleFeedbackPopup: function() {
			var _this = this;
			this.showFeedbackPopup.toggleClass('showFeedbackPopup__opened');
	        this.feedbackPopup.animate({
	            opacity: 'toggle',
	            height: 'toggle',
	            width: 'toggle'
	        }, {
	            duration: 700,
	            specialEasing: {
	                opacity: 'linear',
	                height: 'swing',
	                width: 'swing'
	            },
	            complete: function(){
	                _this.feedbackPopup.toggleClass('feedbackPopup__opened');
	            }
	        });
		}
	})
});