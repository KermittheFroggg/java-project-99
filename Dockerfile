FROM gradle:8.1.1-jdk17

WORKDIR /

COPY / .

RUN gradle bootRun

CMD ./build/install/app/bin