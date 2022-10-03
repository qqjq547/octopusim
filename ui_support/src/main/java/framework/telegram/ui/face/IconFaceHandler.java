/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package framework.telegram.ui.face;

import android.content.Context;
import android.text.Spannable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import framework.telegram.ui.R;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public final class IconFaceHandler {
    private IconFaceHandler() {
    }

    public static Map<String, IconFaceItem> DATA = new LinkedHashMap<>();
    public static List<IconFaceItem> DATA_SORT = new ArrayList<>();
    public static IconFaceItem DELETE = new IconFaceItem("[Delete]",
            R.drawable.sym_keyboard_delete_holo_dark);

    static {
        DATA_SORT.add(new IconFaceItem("[Smile]", R.drawable.pet_emoji_0));
        DATA_SORT.add(new IconFaceItem("[Curse]", R.drawable.pet_emoji_1));
        DATA_SORT.add(new IconFaceItem("[Color]", R.drawable.pet_emoji_2));
        DATA_SORT.add(new IconFaceItem("[In A Daze]", R.drawable.pet_emoji_3));
        DATA_SORT.add(new IconFaceItem("[Proud]", R.drawable.pet_emoji_4));
        DATA_SORT.add(new IconFaceItem("[Tears]", R.drawable.pet_emoji_5));
        DATA_SORT.add(new IconFaceItem("[Shy]", R.drawable.pet_emoji_6));
        DATA_SORT.add(new IconFaceItem("[Shut Up]", R.drawable.pet_emoji_7));
        DATA_SORT.add(new IconFaceItem("[Sleep]", R.drawable.pet_emoji_8));
        DATA_SORT.add(new IconFaceItem("[Cry]", R.drawable.pet_emoji_9));
        DATA_SORT.add(new IconFaceItem("[Embarrassed]", R.drawable.pet_emoji_10));
        DATA_SORT.add(new IconFaceItem("[Angry]", R.drawable.pet_emoji_11));
        DATA_SORT.add(new IconFaceItem("[Naughty]", R.drawable.pet_emoji_12));
        DATA_SORT.add(new IconFaceItem("[Baby Teeth]", R.drawable.pet_emoji_13));
        DATA_SORT.add(new IconFaceItem("[Surprised]", R.drawable.pet_emoji_14));
        DATA_SORT.add(new IconFaceItem("[Sad]", R.drawable.pet_emoji_15));
        DATA_SORT.add(new IconFaceItem("[Cool]", R.drawable.pet_emoji_16));
        DATA_SORT.add(new IconFaceItem("[Cold Sweat]", R.drawable.pet_emoji_17));
        DATA_SORT.add(new IconFaceItem("[Crazy]", R.drawable.pet_emoji_18));
        DATA_SORT.add(new IconFaceItem("[Spit]", R.drawable.pet_emoji_19));
        DATA_SORT.add(new IconFaceItem("[Laughing]", R.drawable.pet_emoji_20));
        DATA_SORT.add(new IconFaceItem("[Happy]", R.drawable.pet_emoji_21));
        DATA_SORT.add(new IconFaceItem("[White Eyes]", R.drawable.pet_emoji_22));
        DATA_SORT.add(new IconFaceItem("[Arrogant]", R.drawable.pet_emoji_23));
        DATA_SORT.add(new IconFaceItem("[Hunger]", R.drawable.pet_emoji_24));
        DATA_SORT.add(new IconFaceItem("[Sleepy]", R.drawable.pet_emoji_25));
        DATA_SORT.add(new IconFaceItem("[Horrified]", R.drawable.pet_emoji_26));
        DATA_SORT.add(new IconFaceItem("[Sweaty]", R.drawable.pet_emoji_27));
        DATA_SORT.add(new IconFaceItem("[Silly Smile]", R.drawable.pet_emoji_28));
        DATA_SORT.add(new IconFaceItem("[Leisure]", R.drawable.pet_emoji_29));
        DATA_SORT.add(new IconFaceItem("[Struggle]", R.drawable.pet_emoji_30));
        DATA_SORT.add(new IconFaceItem("[Cursing]", R.drawable.pet_emoji_31));
        DATA_SORT.add(new IconFaceItem("[Questioning]", R.drawable.pet_emoji_32));
        DATA_SORT.add(new IconFaceItem("[Shush]", R.drawable.pet_emoji_33));
        DATA_SORT.add(new IconFaceItem("[Halo]", R.drawable.pet_emoji_34));
        DATA_SORT.add(new IconFaceItem("[Torture]", R.drawable.pet_emoji_35));
        DATA_SORT.add(new IconFaceItem("[Unhappy]", R.drawable.pet_emoji_36));
        DATA_SORT.add(new IconFaceItem("[Skeleton]", R.drawable.pet_emoji_37));
        DATA_SORT.add(new IconFaceItem("[Beating]", R.drawable.pet_emoji_38));
        DATA_SORT.add(new IconFaceItem("[Goodbye]", R.drawable.pet_emoji_39));
        DATA_SORT.add(new IconFaceItem("[Wiping Sweat]", R.drawable.pet_emoji_40));
        DATA_SORT.add(new IconFaceItem("[Nosing]", R.drawable.pet_emoji_41));
        DATA_SORT.add(new IconFaceItem("[Applauding]", R.drawable.pet_emoji_42));
        DATA_SORT.add(new IconFaceItem("[Embarrassing]", R.drawable.pet_emoji_43));
        DATA_SORT.add(new IconFaceItem("[Smiling]", R.drawable.pet_emoji_44));
        DATA_SORT.add(new IconFaceItem("[Left Humming]", R.drawable.pet_emoji_45));
        DATA_SORT.add(new IconFaceItem("[Right Humming]", R.drawable.pet_emoji_46));
        DATA_SORT.add(new IconFaceItem("[Yawn]", R.drawable.pet_emoji_47));
        DATA_SORT.add(new IconFaceItem("[Contempt]", R.drawable.pet_emoji_48));
        DATA_SORT.add(new IconFaceItem("[Grievance]", R.drawable.pet_emoji_49));
        DATA_SORT.add(new IconFaceItem("[Crying]", R.drawable.pet_emoji_50));
        DATA_SORT.add(new IconFaceItem("[Insidious]", R.drawable.pet_emoji_51));
        DATA_SORT.add(new IconFaceItem("[Kiss]", R.drawable.pet_emoji_52));
        DATA_SORT.add(new IconFaceItem("[Scare]", R.drawable.pet_emoji_53));
        DATA_SORT.add(new IconFaceItem("[Poor]", R.drawable.pet_emoji_54));
        DATA_SORT.add(new IconFaceItem("[Chopping Knife]", R.drawable.pet_emoji_55));
        DATA_SORT.add(new IconFaceItem("[Watermelon]", R.drawable.pet_emoji_56));
        DATA_SORT.add(new IconFaceItem("[Beer]", R.drawable.pet_emoji_57));
        DATA_SORT.add(new IconFaceItem("[Basketball]", R.drawable.pet_emoji_58));
        DATA_SORT.add(new IconFaceItem("[Ping Pong]", R.drawable.pet_emoji_59));
        DATA_SORT.add(new IconFaceItem("[Coffee]", R.drawable.pet_emoji_60));
        DATA_SORT.add(new IconFaceItem("[Rice]", R.drawable.pet_emoji_61));
        DATA_SORT.add(new IconFaceItem("[Pig's Head]", R.drawable.pet_emoji_62));
        DATA_SORT.add(new IconFaceItem("[Rose]", R.drawable.pet_emoji_63));
        DATA_SORT.add(new IconFaceItem("[Fading]", R.drawable.pet_emoji_64));
        DATA_SORT.add(new IconFaceItem("[Lips]", R.drawable.pet_emoji_65));
        DATA_SORT.add(new IconFaceItem("[Heart]", R.drawable.pet_emoji_66));
        DATA_SORT.add(new IconFaceItem("[Heartbreak]", R.drawable.pet_emoji_67));
        DATA_SORT.add(new IconFaceItem("[Cake]", R.drawable.pet_emoji_68));
        DATA_SORT.add(new IconFaceItem("[Lightning]", R.drawable.pet_emoji_69));
        DATA_SORT.add(new IconFaceItem("[Bomb]", R.drawable.pet_emoji_70));
        DATA_SORT.add(new IconFaceItem("[Knife]", R.drawable.pet_emoji_71));
        DATA_SORT.add(new IconFaceItem("[Football]", R.drawable.pet_emoji_72));
        DATA_SORT.add(new IconFaceItem("[Ladybug]", R.drawable.pet_emoji_73));
        DATA_SORT.add(new IconFaceItem("[Poo]", R.drawable.pet_emoji_74));
        DATA_SORT.add(new IconFaceItem("[Moon]", R.drawable.pet_emoji_75));
        DATA_SORT.add(new IconFaceItem("[Sun]", R.drawable.pet_emoji_76));
        DATA_SORT.add(new IconFaceItem("[Gift]", R.drawable.pet_emoji_77));
        DATA_SORT.add(new IconFaceItem("[Hug]", R.drawable.pet_emoji_78));
        DATA_SORT.add(new IconFaceItem("[Strong]", R.drawable.pet_emoji_79));
        DATA_SORT.add(new IconFaceItem("[Weak]", R.drawable.pet_emoji_80));
        DATA_SORT.add(new IconFaceItem("[Handshake]", R.drawable.pet_emoji_81));
        DATA_SORT.add(new IconFaceItem("[Victory]", R.drawable.pet_emoji_82));
        DATA_SORT.add(new IconFaceItem("[Clasped]", R.drawable.pet_emoji_83));
        DATA_SORT.add(new IconFaceItem("[Seduce]", R.drawable.pet_emoji_84));
        DATA_SORT.add(new IconFaceItem("[Fist]", R.drawable.pet_emoji_85));
        DATA_SORT.add(new IconFaceItem("[No Good]", R.drawable.pet_emoji_86));
        DATA_SORT.add(new IconFaceItem("[Love You]", R.drawable.pet_emoji_87));
        DATA_SORT.add(new IconFaceItem("[NO]", R.drawable.pet_emoji_88));
        DATA_SORT.add(new IconFaceItem("[OK]", R.drawable.pet_emoji_89));
        DATA_SORT.add(new IconFaceItem("[Double Happiness]", R.drawable.pet_emoji_90));
        DATA_SORT.add(new IconFaceItem("[Firecrackers]", R.drawable.pet_emoji_91));
        DATA_SORT.add(new IconFaceItem("[Lantern]", R.drawable.pet_emoji_92));
        DATA_SORT.add(new IconFaceItem("[Microphone]", R.drawable.pet_emoji_93));
        DATA_SORT.add(new IconFaceItem("[Celebration]", R.drawable.pet_emoji_94));
        DATA_SORT.add(new IconFaceItem("[Banana]", R.drawable.pet_emoji_95));
        DATA_SORT.add(new IconFaceItem("[Breakout]", R.drawable.pet_emoji_96));
        DATA_SORT.add(new IconFaceItem("[Lollipop]", R.drawable.pet_emoji_97));
        DATA_SORT.add(new IconFaceItem("[Bottle]", R.drawable.pet_emoji_98));
        DATA_SORT.add(new IconFaceItem("[Airplane]", R.drawable.pet_emoji_99));
        DATA_SORT.add(new IconFaceItem("[Car]", R.drawable.pet_emoji_100));
        DATA_SORT.add(new IconFaceItem("[High Speed Rail]", R.drawable.pet_emoji_101));
        DATA_SORT.add(new IconFaceItem("[Gun]", R.drawable.pet_emoji_102));
        DATA_SORT.add(new IconFaceItem("[Balloon]", R.drawable.pet_emoji_103));
        DATA_SORT.add(new IconFaceItem("[Mail]", R.drawable.pet_emoji_104));
        DATA_SORT.add(new IconFaceItem("[Panda]", R.drawable.pet_emoji_105));
        DATA_SORT.add(new IconFaceItem("[Frog]", R.drawable.pet_emoji_106));
        DATA_SORT.add(new IconFaceItem("[Light Bulb]", R.drawable.pet_emoji_107));
        DATA_SORT.add(new IconFaceItem("[Umbrella]", R.drawable.pet_emoji_108));
        DATA_SORT.add(new IconFaceItem("[Chess]", R.drawable.pet_emoji_109));
        DATA_SORT.add(new IconFaceItem("[Mahjong]", R.drawable.pet_emoji_110));
        DATA_SORT.add(new IconFaceItem("[Money]", R.drawable.pet_emoji_111));
        DATA_SORT.add(new IconFaceItem("[Eating Noodles]", R.drawable.pet_emoji_112));
        DATA_SORT.add(new IconFaceItem("[Alarm Clock]", R.drawable.pet_emoji_113));
        DATA_SORT.add(new IconFaceItem("[Pills]", R.drawable.pet_emoji_114));
        DATA_SORT.add(new IconFaceItem("[Cloudy]", R.drawable.pet_emoji_115));
        DATA_SORT.add(new IconFaceItem("[Raining]", R.drawable.pet_emoji_116));
        DATA_SORT.add(new IconFaceItem("[Paper Towel]", R.drawable.pet_emoji_117));
        DATA_SORT.add(new IconFaceItem("[Windmill]", R.drawable.pet_emoji_118));
        DATA_SORT.add(new IconFaceItem("[Sofa]", R.drawable.pet_emoji_119));
        DATA_SORT.add(new IconFaceItem("[Prayer]", R.drawable.pet_emoji_120));

        DATA.put("[Smile]", DATA_SORT.get(0));
        DATA.put("[Curse]", DATA_SORT.get(1));
        DATA.put("[Color]", DATA_SORT.get(2));
        DATA.put("[In A Daze]", DATA_SORT.get(3));
        DATA.put("[Proud]", DATA_SORT.get(4));
        DATA.put("[Tears]", DATA_SORT.get(5));
        DATA.put("[Shy]", DATA_SORT.get(6));
        DATA.put("[Shut Up]", DATA_SORT.get(7));
        DATA.put("[Sleep]", DATA_SORT.get(8));
        DATA.put("[Cry]", DATA_SORT.get(9));
        DATA.put("[Embarrassed]", DATA_SORT.get(10));
        DATA.put("[Angry]", DATA_SORT.get(11));
        DATA.put("[Naughty]", DATA_SORT.get(12));
        DATA.put("[Baby Teeth]", DATA_SORT.get(13));
        DATA.put("[Surprised]", DATA_SORT.get(14));
        DATA.put("[Sad]", DATA_SORT.get(15));
        DATA.put("[Cool]", DATA_SORT.get(16));
        DATA.put("[Cold Sweat]", DATA_SORT.get(17));
        DATA.put("[Crazy]", DATA_SORT.get(18));
        DATA.put("[Spit]", DATA_SORT.get(19));
        DATA.put("[Laughing]", DATA_SORT.get(20));
        DATA.put("[Happy]", DATA_SORT.get(21));
        DATA.put("[White Eyes]", DATA_SORT.get(22));
        DATA.put("[Arrogant]", DATA_SORT.get(23));
        DATA.put("[Hunger]", DATA_SORT.get(24));
        DATA.put("[Sleepy]", DATA_SORT.get(25));
        DATA.put("[Horrified]", DATA_SORT.get(26));
        DATA.put("[Sweaty]", DATA_SORT.get(27));
        DATA.put("[Silly Smile]", DATA_SORT.get(28));
        DATA.put("[Leisure]", DATA_SORT.get(29));
        DATA.put("[Struggle]", DATA_SORT.get(30));
        DATA.put("[Cursing]", DATA_SORT.get(31));
        DATA.put("[Questioning]", DATA_SORT.get(32));
        DATA.put("[Shush]", DATA_SORT.get(33));
        DATA.put("[Halo]", DATA_SORT.get(34));
        DATA.put("[Torture]", DATA_SORT.get(35));
        DATA.put("[Unhappy]", DATA_SORT.get(36));
        DATA.put("[Skeleton]", DATA_SORT.get(37));
        DATA.put("[Beating]", DATA_SORT.get(38));
        DATA.put("[Goodbye]", DATA_SORT.get(39));
        DATA.put("[Wiping Sweat]", DATA_SORT.get(40));
        DATA.put("[Nosing]", DATA_SORT.get(41));
        DATA.put("[Applauding]", DATA_SORT.get(42));
        DATA.put("[Embarrassing]", DATA_SORT.get(43));
        DATA.put("[Smiling]", DATA_SORT.get(44));
        DATA.put("[Left Humming]", DATA_SORT.get(45));
        DATA.put("[Right Humming]", DATA_SORT.get(46));
        DATA.put("[Yawn]", DATA_SORT.get(47));
        DATA.put("[Contempt]", DATA_SORT.get(48));
        DATA.put("[Grievance]", DATA_SORT.get(49));
        DATA.put("[Crying]", DATA_SORT.get(50));
        DATA.put("[Insidious]", DATA_SORT.get(51));
        DATA.put("[Kiss]", DATA_SORT.get(52));
        DATA.put("[Scare]", DATA_SORT.get(53));
        DATA.put("[Poor]", DATA_SORT.get(54));
        DATA.put("[Chopping Knife]", DATA_SORT.get(55));
        DATA.put("[Watermelon]", DATA_SORT.get(56));
        DATA.put("[Beer]", DATA_SORT.get(57));
        DATA.put("[Basketball]", DATA_SORT.get(58));
        DATA.put("[Ping Pong]", DATA_SORT.get(59));
        DATA.put("[Coffee]", DATA_SORT.get(60));
        DATA.put("[Rice]", DATA_SORT.get(61));
        DATA.put("[Pig's Head]", DATA_SORT.get(62));
        DATA.put("[Rose]", DATA_SORT.get(63));
        DATA.put("[Fading]", DATA_SORT.get(64));
        DATA.put("[Lips]", DATA_SORT.get(65));
        DATA.put("[Heart]", DATA_SORT.get(66));
        DATA.put("[Heartbreak]", DATA_SORT.get(67));
        DATA.put("[Cake]", DATA_SORT.get(68));
        DATA.put("[Lightning]", DATA_SORT.get(69));
        DATA.put("[Bomb]", DATA_SORT.get(70));
        DATA.put("[Knife]", DATA_SORT.get(71));
        DATA.put("[Football]", DATA_SORT.get(72));
        DATA.put("[Ladybug]", DATA_SORT.get(73));
        DATA.put("[Poo]", DATA_SORT.get(74));
        DATA.put("[Moon]", DATA_SORT.get(75));
        DATA.put("[Sun]", DATA_SORT.get(76));
        DATA.put("[Gift]", DATA_SORT.get(77));
        DATA.put("[Hug]", DATA_SORT.get(78));
        DATA.put("[Strong]", DATA_SORT.get(79));
        DATA.put("[Weak]", DATA_SORT.get(80));
        DATA.put("[Handshake]", DATA_SORT.get(81));
        DATA.put("[Victory]", DATA_SORT.get(82));
        DATA.put("[Clasped]", DATA_SORT.get(83));
        DATA.put("[Seduce]", DATA_SORT.get(84));
        DATA.put("[Fist]", DATA_SORT.get(85));
        DATA.put("[No Good]", DATA_SORT.get(86));
        DATA.put("[Love You]", DATA_SORT.get(87));
        DATA.put("[NO]", DATA_SORT.get(88));
        DATA.put("[OK]", DATA_SORT.get(89));
        DATA.put("[Double Happiness]", DATA_SORT.get(90));
        DATA.put("[Firecrackers]", DATA_SORT.get(91));
        DATA.put("[Lantern]", DATA_SORT.get(92));
        DATA.put("[Microphone]", DATA_SORT.get(93));
        DATA.put("[Celebration]", DATA_SORT.get(94));
        DATA.put("[Banana]", DATA_SORT.get(95));
        DATA.put("[Breakout]", DATA_SORT.get(96));
        DATA.put("[Lollipop]", DATA_SORT.get(97));
        DATA.put("[Bottle]", DATA_SORT.get(98));
        DATA.put("[Airplane]", DATA_SORT.get(99));
        DATA.put("[Car]", DATA_SORT.get(100));
        DATA.put("[High Speed Rail]", DATA_SORT.get(101));
        DATA.put("[Gun]", DATA_SORT.get(102));
        DATA.put("[Balloon]", DATA_SORT.get(103));
        DATA.put("[Mail]", DATA_SORT.get(104));
        DATA.put("[Panda]", DATA_SORT.get(105));
        DATA.put("[Frog]", DATA_SORT.get(106));
        DATA.put("[Light Bulb]", DATA_SORT.get(107));
        DATA.put("[Umbrella]", DATA_SORT.get(108));
        DATA.put("[Chess]", DATA_SORT.get(109));
        DATA.put("[Mahjong]", DATA_SORT.get(110));
        DATA.put("[Money]", DATA_SORT.get(111));
        DATA.put("[Eating Noodles]", DATA_SORT.get(112));
        DATA.put("[Alarm Clock]", DATA_SORT.get(113));
        DATA.put("[Pills]", DATA_SORT.get(114));
        DATA.put("[Cloudy]", DATA_SORT.get(115));
        DATA.put("[Raining]", DATA_SORT.get(116));
        DATA.put("[Paper Towel]", DATA_SORT.get(117));
        DATA.put("[Windmill]", DATA_SORT.get(118));
        DATA.put("[Sofa]", DATA_SORT.get(119));
        DATA.put("[Prayer]", DATA_SORT.get(120));
    }

    public static void addIconFaces(Context context, Spannable text, float iconFaceSize, float textSize, int start, int length) {
        String input = text.toString().substring(start, start + length);
        if (!IconFaceHandler.DELETE.name.equals(input)) {
            Pattern pattern = Pattern.compile("\\[(\\s?\\w?[^\\x00-\\xff]?)+\\]");
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                String faceName = matcher.group();
                if (faceName != null) {
                    IconFaceItem faceItem = DATA.get(faceName);
                    if (faceItem != null) {
                        text.setSpan(new IconFaceSpan(context, faceItem.resId, (int) iconFaceSize, (int) textSize), matcher.start() + start, matcher.end() + start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        } else {
            IconFaceItem faceItem = IconFaceHandler.DELETE;
            text.setSpan(new IconFaceSpan(context, faceItem.resId, (int) iconFaceSize, (int) textSize), start, IconFaceHandler.DELETE.name.length() + start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
