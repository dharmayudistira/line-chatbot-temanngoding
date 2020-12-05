package com.pandeka.TemanNgoding.model;

public class JointEvents {

    public Long id;

    public String event_id;

    public String user_id;
    public String line_id;
    public String display_name;

    public JointEvents(Long id, String event_id, String user_id, String line_id, String display_name) {
        this.id = id;
        this.event_id = event_id;
        this.user_id = user_id;
        this.line_id = line_id;
        this.display_name = display_name;
    }

    public JointEvents() {
    }
}
