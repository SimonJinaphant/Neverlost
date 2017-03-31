#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define RS232_Control_BT (*(volatile unsigned char *)(0x84000220))
#define RS232_Status_BT (*(volatile unsigned char *)(0x84000220))
#define RS232_TxData_BT (*(volatile unsigned char *)(0x84000222))
#define RS232_RxData_BT (*(volatile unsigned char *)(0x84000222))
#define RS232_Baud_BT (*(volatile unsigned char *)(0x84000224))
#define RS232_Control_HR (*(volatile unsigned char *)(0x84000250))
#define RS232_Status_HR (*(volatile unsigned char *)(0x84000250))
#define RS232_TxData_HR (*(volatile unsigned char *)(0x84000252))
#define RS232_RxData_HR (*(volatile unsigned char *)(0x84000252))
#define RS232_Baud_HR (*(volatile unsigned char *)(0x84000254))
#define RS232_Control_GPS (*(volatile unsigned char *)(0x84000210))
#define RS232_Status_GPS (*(volatile unsigned char *)(0x84000210))
#define RS232_TxData_GPS (*(volatile unsigned char *)(0x84000212))
#define RS232_RxData_GPS (*(volatile unsigned char *)(0x84000212))
#define RS232_Baud_GPS (*(volatile unsigned char *)(0x84000214))

typedef struct { char* lat; char* lng; } Coord ;

//initialize
void initRS232_GPS(void) {
	//8 data bits, 1 stop bit
	RS232_Control_GPS = 0x15;

	//9600 baud
	RS232_Baud_GPS = 0x05;
}

//read from rx
char getCharRS232_GPS(void) {
	while (!(RS232_Status_GPS & 0x01)) {
	};
	return RS232_RxData_GPS;
}

//write to tx
void putCharRS232_GPS(int c) {
	while (!(0x02 & RS232_Status_GPS)) {
	};
	RS232_TxData_GPS = c;
}

Coord getGPSdata() {
    int size = 500;
    Coord c;
    while (1) {
        char data[size];
        char character;
        int index = 0;
        const char s[2] = ",";
        //get data string
        do {
            character = getCharRS232_GPS();
            printf("%c", character);
            data[index] = character;
            index++;
        } while (character != '\n');

        char *token = strtok(data, s);

        if (!strcmp(token, "$GPGGA")) {
            printf("message_id: %s \n", token);

            token = strtok(NULL, s);
            double UTC_time = atof(token);
            printf("UTC_time: %f \n", UTC_time);

            token = strtok(NULL, s);
            char* latitude = token;
            printf("latitude: %s \n", latitude);

            token = strtok(NULL, s);
            char* NS_indicator = token;

            token = strtok(NULL, s);
            char* longitude = token;
            printf("longitude: %s \n", longitude);

            token = strtok(NULL, s);
            char* EW_indicator = token;

            double lat_double = atof(latitude);
            double long_double = atof(longitude);

            if (strcmp(NS_indicator, "S") == 0){
            	lat_double = lat_double*-1;
            }

            if (strcmp(EW_indicator, "W") == 0){
            	long_double = long_double*-1;
            }

            int lat_degree = (int) (lat_double/100);
            double lat_min = (lat_double-(lat_degree*100))/60;
            double lat_converted = lat_degree+lat_min;
            int long_degree = (int) (long_double/100);
            double long_min = (long_double-(long_degree*100))/60;
            double long_converted = long_degree+long_min;

            char *lat_char = malloc(100*sizeof(char));
            char *long_char = malloc(100*sizeof(char));
            snprintf(lat_char, 100, "%f", lat_converted);
            snprintf(long_char, 100, "%f", long_converted);

            c.lat = lat_char;
            c.lng = long_char;

            return c;
        }
    }
    return c;
}


char* concat(const char *s1, const char *s2, const char *s3, const char *s4){
    char *result = malloc(strlen(s1)+strlen(s2)+strlen(s3)+strlen(s4)+1);
    strcpy(result, s1);
    strcat(result, s2);
    strcat(result, s3);
    strcat(result, s4);
    return result;
}

void initRS232_BT(void){
	//8 data bits, 1 stop bit
	RS232_Control_BT = 0x15;

	RS232_Baud_BT = 0x01;
}

//write to tx
char getCharRS232_BT(void) {
	while (!(RS232_Status_BT & 0x01)) {
	};
	return RS232_RxData_BT;
}

//read from rx
void putCharRS232_BT(int c) {
	while (!(0x02 & RS232_Status_BT)) {
	};
	RS232_TxData_BT = c;
}

//initialize
void initRS232_HR(void) {
	//8 data bits, 1 stop bit
	RS232_Control_HR = 0x15;

	//9600 baud
	RS232_Baud_HR = 0x05;
}

//read from rx
char getCharRS232_HR(void) {
	while (!(RS232_Status_HR & 0x01)) {
	};
	return RS232_RxData_HR;
}

//write to tx
void putCharRS232_HR(int c) {
	while (!(0x02 & RS232_Status_HR)) {
	};
	RS232_TxData_HR = c;
}

void waitChar(int i){
	int j;
	for (j = 0; j < i; j++)
		getCharRS232_HR();
}

int getHR(){
	char HR_MSB;
	char HR_LSB;
	while (1){
		char s = getCharRS232_HR();
		if ((!(s&0xFE))&(s&0x01)){
			if (getCharRS232_HR()&0x01){
				waitChar(1);
				HR_MSB = getCharRS232_HR();
				waitChar(4);
				HR_LSB = getCharRS232_HR();

				int HR = (0x7F&HR_LSB)+((0x03&HR_MSB)<<7);

				return HR;
			}
		}
	}
	return 0;
}

int getHRData(){
	int i;
	int j;
	do{
		i = getHR();
		j = getHR();
	}
	while(i != j);
	printf("\n heart rate data: %d\n", i);
	return i;
}

void sendString(char* string){
	int i = 0;
	while (string[i] != NULL){
		putCharRS232_BT(string[i]);
		i++;
	}
	putCharRS232_BT('\r');
	putCharRS232_BT('\n');
}

char* readString(){
	char *s = malloc(30);

	int i = 0;
	char c = getCharRS232_BT();
	while(c != '\r'){
		s[i] = c;
		i++;
		printf("%c", c);
		c = getCharRS232_BT();
	}
	getCharRS232_BT();
	s[i] = '\0';
	return s;
}

int main() {
	initRS232_BT();
	initRS232_HR();
	initRS232_GPS();
	while(1){
		char *key = readString();
		if (!strcmp(key, "HeartRate")){
			char* buffer = malloc(30);
			snprintf(buffer, 30, "%d", getHRData());
			sendString(buffer);
		}
		else if (!strcmp(key, "Latitude")){
			Coord c = getGPSdata();
			sendString(c.lat);
		}
		else if (!strcmp(key, "Longitude")){
			Coord c = getGPSdata();
			sendString(c.lng);
		}
	}
}