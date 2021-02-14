const app = Vue.createApp({

    data() {
        return {
            hospitals: [

            ],
            seekername: "Tester", //for debugging
        };
    },
    computed: {

    },
    watch: {
    },
    methods: {
        setTotalBeds() { },

        typeAsString: function (intType) {
            switch (intType) {
                case 0:
                    return "Empty";
                    break;
                case 1:
                    return "Covid Specific"
                    break;
                case 2:
                    return "Emergency";
                    break;
                case 3:
                    return "Converted";
                    break
            }
        },

        reloadInformation() {
            hospitals = [];
            fetch("/avaliableHospitals/", {
                "method": "GET",
                "headers": {
                    'Access-Control-Allow-Origin': '*'
                }
            }).then(response => {
                var data = response.json();
                return data;
            }).then(data => {

                data.forEach(element => {
                    console.log(element);
                    this.hospitals.push(element);
                });
            })


        },


        pullSoughtHospital() {  //debug
            var name = this.seekername;
            fetch("/hospital/" + name, {
                "method": "GET",
                "headers": {}
            }).then(response => {
                var data = response.json();
                return data;
            }).then(data => {
                console.log(data);
                this.hospitals.push(data);
            })
        },

        setChanger(event) {  //dedbugging method
            this.seekername = event.target.value;
            console.log(this.seekername);
        }

    }, mounted() {
        this.reloadInformation();

    }




});
app.mount("#UserStatus");