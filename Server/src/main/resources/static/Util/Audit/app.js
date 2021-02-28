const app = Vue.createApp({

    data() {
        return {
            numberOfChanges: 10,
            changer: 0,
            changes: [

            ],
            startingElement: 0,
            clearer: "",
            pageNum: 1,
            oldIndex: 0
        };
    },
    computed: {
        checkIfDiffDay() {

            var dex = this.oldIndex;
            if (dex == this.changes.length - 1) {
                return false;
            }
            var olday = this.changes[dex].dateOfChange.substring(0, 10);
            var newday = this.changes[dex + 1].dateOfChange.substring(0, 10);

            return olday != newday;
        },
    },
    watch: {
    },

    methods: {

        convertedDate: function (milisec) {
            var date = new Date(milisec);
            return date.toLocaleDateString("en-GB");
        },


        setOldChange(index) {
            this.oldIndex = index;
        },



        convertedTime: function (milisec) {
            var date = new Date(milisec);
            return (date.getHours() + ":" + date.getMinutes());
        },

        getChanges() {
            this.changes = [];

            fetch("/Audit/" + this.numberOfChanges + "/" + this.startingElement, {
                "method": "GET",
                "headers": {},

            }).then(response => {
                var data = response.json();
                return data;
            }).then(data => {
                data.forEach(element => {
                    this.changes.push(element);
                });

            })


        },
        setNumberOfChangesTemp(event) {
            this.changer = event.target.value;
        },
        confirmNumberOfChanges() {
            this.numberOfChanges = this.changer;
            this.getChanges();
        },
        lastPage() {
            this.pageNum--;
            if (this.pageNum <= 0) {
                this.pageNum = 1;
            }
            this.startingElement -= this.numberOfChanges;
            if (this.startingElement < 0) {
                this.startingElement = 0;
            }
            this.getChanges();
        },

        nextPage() {
            this.pageNum++;
            this.startingElement += parseInt(this.numberOfChanges);
            console.log(this.startingElement);
            this.getChanges();
        },




    }, mounted() {
        this.getChanges();

    }




});
app.mount("#Audit");

