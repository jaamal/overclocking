Function.isFunction = function Function$isFunction(obj) {
    return obj && obj.constructor == Function;
};

var allHandlers = [];

var Event = function(implementation) {
    this.implementation = implementation || function() { this.handle(); };
    this.handlers = [];
    allHandlers.push(this.handlers);
};

$.extend(Event.prototype, {
    addHandler: function(handler, context) {
        this.handlers.push({ handlerFunc: handler, context: context || this });
    },
    removeHandler: function(handlerToRemove) {
        var newHandlers = [];
        for (var i = 0; i < this.handlers.length; i++) {
            var handler = this.handlers[i];
            if (handler.handlerFunc != handlerToRemove && handler.context != handlerToRemove) {
                newHandlers.push(handler);
            }
        }
        this.handlers = newHandlers;
    },
    handle: function(args) {
        for (var i = 0; i < this.handlers.length; i++) {
            var handler = this.handlers[i];
            if (handler.handlerFunc.apply(handler.context, args) === false)
                break;
        }
    },
    fire: function() {
        var thisEvent = this;
        var handler = {
            handle: function() {
                thisEvent.handle(this.arguments);
            },
            arguments: arguments
        };
        this.implementation.apply(handler, arguments);
    }
});

Event.create = function (implementation) {
    var event = new Event(implementation);
    return function(handler) {
        if (Function.isFunction(handler))
            event.addHandler.apply(event, arguments);
        else
            event.fire.apply(event, arguments);
    };
};
Event.removeHandlers = function() {
    if (allHandlers) {
        for (var i = 0; i < allHandlers.length; i++) {
            for (var j = 0; j < allHandlers[i].length; j++) {
                allHandlers[i][j] = null;
            }
            allHandlers[i] = null;
        }
        allHandlers = [];
    }
};
