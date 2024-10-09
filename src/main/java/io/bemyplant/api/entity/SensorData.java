package io.bemyplant.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "#{this.sensorId}")
public class SensorData {
    @Override
    public String toString() {
        return "SensorData{" +
                ", aTemperature=" + airTemp +
                ", aHumidity=" + airHumid +
                ", sHumidity=" + soilHumid+
                ", lightIntensity=" + lightIntensity +
                ", status=" + status +
                ", date=" + date +
                '}';
    }
    @Id
    private ObjectId id;
    //private String sensorId;
    private double airTemp= 0.0;
    private double airHumid = 0.0;
    private double soilHumid = 0.0;
    private double lightIntensity = 0.0;
    private boolean status;
    private Date date;
    public SensorData(/*String sensorId,*/ double airTemp, double airHumid, double soilHumid, double lightIntensity, boolean status, Date date) {
        //this.sensorId = sensorId;
        this.airTemp = airTemp;
        this.airHumid = airHumid;
        this.soilHumid = soilHumid;
        this.lightIntensity = lightIntensity;
        this.status = status;
        this.date = date;
    }
    public String getId() {
        return id.toHexString();
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

}
