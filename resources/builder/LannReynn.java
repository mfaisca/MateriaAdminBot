package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LannReynn extends _Default{
	public LannReynn() {
		super("Lann&Reynn", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann", "lann");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{6840};
				break;
			case HP:
				ids = new int[]{6844};
				break;
			case S1:
				ids = new int[]{6833};
				break;
			case S2:
				ids = new int[]{6836, 6837};
				break;
			case EX:
				ids = new int[]{6843};
				break;
			case AA:
				ids = new int[]{6831};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}