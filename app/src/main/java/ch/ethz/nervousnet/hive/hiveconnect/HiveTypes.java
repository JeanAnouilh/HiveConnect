package ch.ethz.nervousnet.hive.hiveconnect;

import java.util.Map;

/**
 * Created by Dario on 28.08.17.
 */

public class HiveTypes {
    //Public Types
    //
    //    ---- Public data types
    //

    class Params {
        public String from;
        public String size;
        public String sortBy;
        public String sortDir;
        public String task;
        public String state;
        public String verified;
    }

    class Meta {
        public int Total;
        public int From;
        public int Size;
    }

    class Project {
        public String id;
        public String name;
        public String description;
        public int assetCount;
        public int taskCount;
        public int userCount;
        public Counts assignmentCount;
        public MetaProperty[] metaProperties;
    }

    class Task {
        public String id;
        public String project;
        public String name;
        public String description;
        public String currentState;
        public AssignmentCriteria assignmentCriteria;
        public CompletionCriteria completionCriteria;
    }

    class Asset {
        public String id;
        public String project;
        public String url;
        public String name;
        public Map<String, Object> metadata;
        public SubmittedData submittedData;
        public boolean favorited;
        public boolean verified;
        public Counts counts;
    }

    class User {
        public String id;
        public String name;
        public String email;
        public String project;
        public String externalId;
        public Counts counts;
        public UserFavorites favorites;
        public UserFavorites newFavorites;
        public String[] verifiedAssets;
    }

    class Assignment {
        public String id;
        public String user;
        public String project;
        public String task;
        public Asset asset;
        public String state;
        public SubmittedData submittedData;
    }

    class Counts {
        public Map<String, Integer> metadata;
    }

    class MetaProperty {
        public String name;
        public String type;
    }

    class AssignmentCriteria {
        public Map<String, Object> submittedData;
    }

    class CompletionCriteria {
        public int total;
        public int matching;
    }

    class SubmittedData {
        public Map<String, Object> submittedData;
    }

    class UserFavorites {
        public Map<String, Asset> userFavorites;
    }
}
