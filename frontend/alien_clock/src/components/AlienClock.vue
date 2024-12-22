<template>
	<div class="container mt-4">
	  <h1 class="text-center mb-4">Alien Clock</h1>
		<div class="p-3 bg-light rounded shadow">
			<h2>Alien Time</h2>
			<p class="display-5">{{ alienTime }}</p>
		</div>
		<div class="p-3 bg-light rounded shadow mt-4">
			<!-- set Alien Time -->
			 <h2>Set Alien Time</h2>
			<form @submit.prevent="setAlienTime">
				<div class="input-group mb-3">
					<input 
						v-model="form.hour"
						@input="validateHour"
						type="number"
						class="form-control"
						placeholder="Hour"
						min="0"
						max="36"
					>
					<input 
						v-model="form.minute"
						@input="validateMinute"
						type="number"
						class="form-control"
						placeholder="Minute"
						min="0"
						max="89"
					>
					<input 
						v-model="form.second"
						@input="validateSecond"
						type="number"
						class="form-control"
						placeholder="Second"
						min="0"
						max="89"
					>
				</div>
				<div class="input-group mb-3">
					<input 
						v-model="form.year"
						@input="validateYear"
						type="number"
						class="form-control"
						placeholder="Year"
						min="2804"
					>
					<input 
						v-model="form.month"
						@input="validateMonth"
						type="number"
						class="form-control"
						placeholder="Month"
						min="1"
						max="18"
					>
					<input 
						v-model="form.day"
						@input="validateDay"
						type="number"
						class="form-control"
						placeholder="Day"
						min="1"
					>
					
					
				</div>
				<div class="d-grid gap-2 ">
					<button type="submit" class="btn btn-primary col" :disabled="!isFormValid">Set Time</button>
					<button type="button" class="btn btn-secondary col" @click="setAlarm" :disabled="!isFormValid">Set Alarm</button>
				</div>
				
			</form>
		</div>

		<div class="p-3 bg-light rounded shadow mt-4">
			<h2>Earth Time</h2>
			<p class="display-5">{{ earthTime }}</p>
		</div>
	</div>

	<div class="container mt-4">
		<h1 class="text-center mb-4">Alarms</h1>
		<li v-for="(alarm, index) in alarms" :key="index" class="list-group">
			<div class="list-group-item d-flex justify-content-between align-items-center rounded shadow p-3">
				{{ alarm }}
				<button @click="deleteAlarm(index)" class="btn btn-danger">Delete</button>
			</div>
		</li>
	</div>
  </template>
<script>
import axios from 'axios';

export default {
	data() {
		return {
			earthTime: '',
			alienTime: '',
			daysInMonth:[44, 42, 48, 40, 48, 44, 40, 44, 42, 40, 40, 42, 44, 48, 42, 40, 44, 38],
			form: {
				hour: undefined,
				minute: undefined,
				second: undefined,
				day: undefined,
				month: undefined,
				year: undefined,
			},
			validationErrors: {},
			alarms: [],
			alarmNotification: null,
			socket: null,
			sessionId: null
		}
	},
	computed:{
		isFormValid(){
			return Object.keys(this.validationErrors).length === 0 && this.form.hour !== undefined && this.form.minute !== undefined && this.form.second !== undefined && this.form.day !== undefined && this.form.month !== undefined && this.form.year !== undefined;
		}
	},
	mounted() {
		setInterval(() => {
			this.getEarthTime();
		}, 1000);
		this.getEarthTime();
		this.getAlienTime();
		setInterval(() => {
			this.getAlienTime();
		}, 500);
		this.connectWebSocket();
	},
	methods:{
		getAlienTime(){
			axios.get('http://localhost:8080/api/alien-clock/current-time').then(response =>{
				const data = response.data;
				this.alienTime = `${String(data.hour).padStart(2, '0')}:${String(data.minute).padStart(2, '0')}:${String(data.second).padStart(2, '0')} ${String(data.month).padStart(2, '0')}/${String(data.day).padStart(2, '0')}/${data.year}`;
			});
		},
		getEarthTime(){
			axios.get('http://localhost:8080/api/alien-clock/earth-time').then(response =>{
				this.earthTime = new Date(response.data).toLocaleTimeString('en-GB') + ' ' + new Date(response.data).toLocaleDateString();
			});
		},
		validateHour(){
			if(this.form.hour < 0 || this.form.hour > 36 || this.form.hour == undefined){
				this.validationErrors.hour = 'Hour must be between 0 and 36.';
			}else{
				delete this.validationErrors.hour;
			}
		},
		validateMinute(){
			if(this.form.minute < 0 || this.form.minute > 89 || this.form.minute == undefined){
				this.validationErrors.minute = 'Minute must be between 0 and 89.';
			}else{
				delete this.validationErrors.minute;
			}
		},
		validateSecond(){
			if(this.form.second < 0 || this.form.second > 89 || this.form.second == undefined){
				this.validationErrors.second = 'Second must be between 0 and 89.';
			}else{
				delete this.validationErrors.second;
			}
		},
		validateDay(){
			if(this.form.day < 1 || this.form.day > this.daysInMonth[this.form.month - 1] || this.form.day == undefined){
				this.validationErrors.day = `Day must be between 1 and ${this.daysInMonth[this.form.month - 1]}`;
			}else{
				delete this.validationErrors.day;
			}
		},
		validateMonth(){
			if(this.form.month < 1 || this.form.month > 18 || this.form.month == undefined){
				this.validationErrors.month = 'Month must be between 1 and 18.';
			}else{
				delete this.validationErrors.month;
			}
		},
		validateYear(){
			if(this.form.year < 2804 || this.form.year === undefined){
				this.validationErrors.year = 'Year must be greater than or equal to 2804.';
			}else{
				delete this.validationErrors.year;
			}
		},
		setAlienTime(){
			axios.post('http://localhost:8080/api/alien-clock/set-time', this.form).then(response =>{
				this.getAlienTime();
				console.log(response.data);
			});
		},
		formatDate(date){
			return `${String(date.hour).padStart(2, '0')}:${String(date.minute).padStart(2, '0')}:${String(date.second).padStart(2, '0')} ${String(date.month).padStart(2, '0')}/${String(date.day).padStart(2, '0')}/${date.year}`;
		},
		getAlarms(){
			axios.get(`http://localhost:8080/api/alarm/get/${this.sessionId}`)
			.then(response => {
				if(response.data.length != 0){
					response.data.forEach(alarm => {
					this.alarms.push(this.formatDate(alarm));
				});
				}
				
				console.log(this.alarms);
			});
		},
		setAlarm(){
			if(this.isFormValid){
				axios.post(`http://localhost:8080/api/alarm/add/${this.sessionId}`, this.form
				).then(response => {
					this.alarms.push(this.formatDate(this.form));
				});

			}
		},
		testAlarm(){
			axios.post(`http://localhost:8080/api/alarm/test/${this.sessionId}`).then(response => {
					console.log("Alarm test successful");
				});
		},
		deleteAlarm(id){
			console.log(id);
			axios.delete(`http://localhost:8080/api/alarm/delete/${this.sessionId}/${id}`).then(response => {
				this.alarms.splice(id, 1);
			});
		},
		connectWebSocket(){
			this.socket = new WebSocket('ws://localhost:8080/alarm');
			this.socket.onmessage = (event) => {
				console.log("WebSocket message received:", event.data);
				const parsed = String(event.data).split(":");
				if(parsed[0] === "alarm"){
					alert(parsed[1] + " triggered");
				}else if(parsed[0] === "sessionID"){
					this.sessionId = parsed[1].trim();
					this.getAlarms();
				}
				
      		};

			this.socket.onopen = () => {
				console.log("WebSocket connection established");
			};

			this.socket.onerror = (error) => {
				console.error("WebSocket error", error);
			};

			this.socket.onclose = () => {
				console.log("WebSocket connection closed");
			};
		}
		
	}
}
</script>
