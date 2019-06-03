package fr.esgi.j2e.group6.captchup.user.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.List;

public class UserSerializer extends StdSerializer {
    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(!(o instanceof User)) {
            throw new IllegalArgumentException();
        }

        User user = (User) o;

        serializeSingleUser(user, jsonGenerator, 1);
    }

    private void serializeSingleUser(User user, JsonGenerator jsonGenerator, int depth) throws IOException {
        jsonGenerator.writeStartObject();

        if(user.getId() == null) {
            jsonGenerator.writeNullField("id");
        }else{
            jsonGenerator.writeNumberField("id", user.getId());
        }
        jsonGenerator.writeStringField("username", user.getUsername());
        jsonGenerator.writeStringField("password", user.getPassword());

        jsonGenerator.writeArrayFieldStart("follow");

        List<User> followUser = user.getFollow();
        if(followUser != null) {
            for(User follow: followUser) {
                if(depth > 2) {
                    jsonGenerator.writeNumber(follow.getId());
                }else{
                    serializeSingleUser(follow, jsonGenerator, depth + 1);
                }
            }
        }

        jsonGenerator.writeEndArray();

        jsonGenerator.writeArrayFieldStart("followedBy");

        List<User> followedByUser = user.getFollowedBy();
        if(followedByUser != null) {
            for(User followedBy: followedByUser) {
                if(depth > 2) {
                    jsonGenerator.writeNumber(followedBy.getId());
                }else{
                    serializeSingleUser(followedBy, jsonGenerator, depth + 1);
                }
            }
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
