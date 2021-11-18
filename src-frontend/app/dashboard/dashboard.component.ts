import {AfterViewInit, Component, OnInit, ViewChild,AfterContentInit} from '@angular/core';
import {ChartOptions, ChartType, ChartDataSets, Chart} from "chart.js";
import {Label} from "ng2-charts";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  public chart: Chart;
  allrequests:any;
  resultRequests:any;
  resultResources:any







constructor(private http:HttpClient ) {


}

  ngOnInit() {
  this.getAllRequest();





  }


  getAllRequest() {
    this.http.get('http://localhost:8080/api/requests').subscribe((data) => {
      console.log(data);
      JSON.stringify(data);
      this.allrequests=data


    let monthNames = [
        "January", "February", "March",
        "April", "May", "June",
        "July", "August", "September",
        "October", "November", "December"
      ];

      let allDatesRequests= this.allrequests.map(e=>e.startDate)
      console.log(allDatesRequests)

      let allDatesResourcesCount = this.allrequests.map(t=>t.resourceDTOS.length)
      console.log(allDatesResourcesCount)

      

      let allDatesResources = allDatesRequests.map((c, i) => Array.from({ length: allDatesResourcesCount[i] }).fill(c)).flat();
      console.log(allDatesResources)

      let groupRequests = allDatesRequests.reduce(function (r, o){
        let date = new Date(o);
        let month = date.getMonth();
        let monthName = monthNames[month];
        (r[monthName]) ? r[monthName].count++ : r[monthName] = { monthName: monthName, count: 1 };
        return r;
      }, {});

      let groupResources = allDatesResources.reduce(function (r, o){
        let date = new Date(o);
        let month = date.getMonth();
        let monthName = monthNames[month];
        (r[monthName]) ? r[monthName].count++ : r[monthName] = { monthName: monthName, count: 1 };
        return r;
      }, {});

      this.resultRequests = Object.keys(groupRequests).map((key) => groupRequests[key]).slice(0,6);
      console.log(this.resultRequests)
      this.resultResources = Object.keys(groupResources).map((key) => groupResources[key]).slice(0,6);
      console.log(this.resultResources)

      this.chart = new Chart("canvas", {
        type: "bar",
        data: {

          labels: this.resultRequests.map(e=>e.monthName) ,
          datasets: [
            {
              label: "Requests per month",
              data: this.resultRequests.map(e=>e.count),
              backgroundColor:"rgba(255, 99, 132, 0.2)",
              borderColor: "rgba(255, 99, 132, 1.0)",
              borderWidth: 1
            },

            {
              label: "Resources per month",
              data: this.resultResources.map(e=>e.count),
              backgroundColor:"rgba(54, 162, 235, 0.2)",
              borderColor: "rgba(54, 162, 235, 1)",
              borderWidth: 1
            }
          ]
        },
        options: {
          plugins:{
            legend:{
              labels:{
                font:{
                  size:30
                }
              }
            }
          },
          responsive: true,
          scales: {
            yAxes: [
              {
                ticks: {
                  beginAtZero: true
                }
              }
            ]
          }
        }
      });

    })


}
}
