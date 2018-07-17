
$(function() {

          $.get("/ssoclient2/user", function(data) {
            $("#user").html(data);
            $(".unauthenticated").hide();
            $(".authenticated").show();
          });

        $.get("/ssoclient2/user2", function(data) {
            $("#user2").html(data);

          });

          });