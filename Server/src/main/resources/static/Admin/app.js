const app = Vue.createApp({

    data() {
        return {
            departments: [],

            changer: 0,

            hospitalName: "",
            clearer: ""
        };
    },
    computed: {

    },
    watch: {

    },
    methods: {

        setChanger(event) {

            var val = event.target.value;

            if (!isNaN(val) && val > 0) {
                this.changer = val;
            }
        },

        setTotal: function (index) {
            if (this.changer != 0) {
                this.departments[index].total = this.changer;
            }
            this.changer = 0;
        },


        setFree: function (index) {
            if (this.changer != 0) {
                this.departments[index].freebeds = this.changer;
            }
            this.changer = 0;
        },
        setOccupied: function (index) {
            if (this.changer != 0) {
                this.departments[index].occupied = this.changer;
            }
            this.changer = 0;
        },
        recalculate: function (index) {

            console.log(this.departments[index].freebeds);

            this.departments[index].freebeds = this.departments[index].total - this.departments[index].occupied;


            if (this.departments[index].freebeds < 0) this.departments[index].freebeds = 0;
        },

        occupyBed: function (index) {
            this.departments[index].occupied++;
            this.departments[index].freebeds--;
        },
        freeBed: function (index) {
            this.departments[index].occupied--;
            this.departments[index].freebeds++;
        },

        pullSoughtHospital() { //debug
            var name = this.hospitalName;
            fetch("/hospital/" + name, {
                "method": "GET",
                "headers": {}
            }).then(response => {
                var data = response.json();
                return data;
            }).then(data => {
                console.log(data)

                this.freeBeds = data.freebeds;
                this.occupied = data.occupied;
                this.totalBeds = data.total;

                ;;
            })
        },

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


        getHospitalName() {


            fetch("/hos-name-user/", {
                "method": "GET",
                "headers": {}
            })

                .then(response => {
                    if (!response.ok) {
                        throw new Error("Hospital name error");

                    }

                    var data = response.text();

                    return data;
                })
                .then(data => {
                    this.hospitalName = data;

                    this.loadDepartaments();
                })

                .catch(error => {
                    console.log(error)
                    location.replace("../Util/PermError/index.html");
                })

        },


        loadDepartaments() {
            console.log("load initiated");

            var name = this.hospitalName;
            fetch("/deps-from-name/" + name, {
                "method": "GET",
                "headers": {}
            }).then(response => {
                var data = response.json();
                return data;
            }).then(data => {
                data.forEach(element => {
                    this.departments.push(element);
                });

            })

        },

        postHospital: function (index) {
            var request = new XMLHttpRequest();
            request.onreadystatechange = function () {
                if (request.readyState == 4) {
                    console.log("Ready");
                }
            }

            var hospital = this.departments[index];

            var json = JSON.stringify(hospital);

            console.log(hospital);
            request.open("POST", "/addition", true);
            request.setRequestHeader("Content-Type", "application/json");
            request.send(json);
        }

    }, mounted() {
        this.getHospitalName();

    }

});

app.mount("#AdminStatus");