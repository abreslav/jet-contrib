{
    var classes = function () {
        var HelloKotlin = Kotlin.Class.create({initialize:function () {
            this.$context = getContext();
            this.$height = getCanvas().height;
            this.$width = getCanvas().width;
        }, get_context:function () {
            return this.$context;
        }, get_height:function () {
            return this.$height;
        }, get_width:function () {
            return this.$width;
        }, run:function () {
            {
                var tmp$0;
                setInterval((tmp$0 = this , function () {
                    {
                        tmp$0.greet();
                    }
                }
                    ), 2000);
            }
        }, greet:function () {
            {
                this.get_context().save();
                var message = 'Hello Kotlin';
                this.get_context().font = 'bold 20px Georgia, serif';
                this.get_context().scale(5, 5);
                this.get_context().strokeStyle = 'rgba(0,0,0, 0.8)';
                this.get_context().strokeText(message, this.get_width() / 7 * Math.random(), this.get_height() / 7 * Math.random());
                this.get_context().restore();
            }
        }
        });
        var Test = Kotlin.Class.create({initialize:function () {
            this.$context = getContext();
            this.$height = getCanvas().height;
            this.$width = getCanvas().width;
            this.$x = this.get_width() * Math.random();
            this.$y = this.get_height() * Math.random();
            this.$hue = 0;
        }, get_context:function () {
            return this.$context;
        }, get_height:function () {
            return this.$height;
        }, get_width:function () {
            return this.$width;
        }, get_x:function () {
            return this.$x;
        }, set_x:function (tmp$0) {
            this.$x = tmp$0;
        }, get_y:function () {
            return this.$y;
        }, set_y:function (tmp$0) {
            this.$y = tmp$0;
        }, get_hue:function () {
            return this.$hue;
        }, set_hue:function (tmp$0) {
            this.$hue = tmp$0;
        }, line:function () {
            {
                this.get_context().save();
                this.get_context().beginPath();
                this.get_context().lineWidth = 20 * Math.random();
                this.get_context().moveTo(this.get_x(), this.get_y());
                this.set_x(this.get_width() * Math.random());
                this.set_y(this.get_height() * Math.random());
                this.get_context().bezierCurveTo(this.get_width() * Math.random(), this.get_height() * Math.random(), this.get_width() * Math.random(), this.get_height() * Math.random(), this.get_x(), this.get_y());
                this.set_hue(this.get_hue() + Math.random() * 10);
                this.get_context().strokeStyle = 'hsl(' + this.get_hue() + ', 50%, 50%)';
                this.get_context().shadowColor = 'white';
                this.get_context().shadowBlur = 10;
                this.get_context().stroke();
                this.get_context().restore();
            }
        }, blank:function () {
            {
                this.get_context().fillStyle = 'rgba(255,255,1,0.1)';
                this.get_context().fillRect(0, 0, this.get_width(), this.get_height());
            }
        }, run:function () {
            {
                var tmp$1;
                var tmp$0;
                setInterval((tmp$0 = this , function () {
                    {
                        tmp$0.line();
                    }
                }
                    ), 40);
                setInterval((tmp$1 = this , function () {
                    {
                        tmp$1.blank();
                    }
                }
                    ), 100);
            }
        }
        });
        return {Test_0:Test, HelloKotlin_0:HelloKotlin};
    }
        ();
    var testhtml = Kotlin.Namespace.create({initialize:function () {
    }, main:function () {
        {
            $(function () {
                    {
                        (new testhtml.Test_0).run();
                    }
                }
            );
        }
    }
    }, classes);
    testhtml.initialize();
}

var args = [];
testhtml.main(args);
